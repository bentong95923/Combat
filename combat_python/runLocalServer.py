#!/usr/bin/python
""" runLocalServer.py

	COMPSYS302 - Software Design
	Author: Benjamin Tong
	Last Edited: 19/02/2015

	This program uses the CherryPy web server (from www.cherrypy.org).
"""
# Requires:  CherryPy 3.2.2  (www.cherrypy.org)
#			Python  (We use 2.7)

# The address we listen for connections on
import cherrypy
import json
import urllib2
import urllib
import socket
import hashlib
import time
import threading
import re
import sqlite3
import sys
import ctypes
import os
import mimetypes
import webbrowser
"""
import base64
from Crypto.Cipher import AES
"""
from threading import Timer

def getIPAddress():
	try:
		# Try to get uni internal IP address
		IPAddress = socket.gethostbyname(socket.getfqdn())
	except:
		# If not successful then try to get the local or external IP address
		IPAddress = socket.gethostbyname(socket.gethostname())
	return IPAddress

listen_ip = getIPAddress()
listen_port = 10035
hash_salt = "COMPSYS302-2016"
databaseFileName = 'combatDatabase.db'
publicEncryptKey = '150ecd12d550d05ad83f18328e536f53'

class MainApp(object):
	global database
	# Create a database for the game
	database = sqlite3.connect(databaseFileName, check_same_thread=False)
	# Using UTF-8 encoding
	database.text_factory = str
	# Create a global database cursor in class to process database enquiries
	global queryCurs, slave, master, firstTime
	queryCurs = database.cursor()
	slave = False
	master = False
	firstTime = True
	
	global loginFieldHtml
	loginFieldHtml = """<form action="/signin" method="post" enctype="multipart/form-data">\n<div class="loginText">\nLogin:\n<br>\n</div>\n\t\t<input type="text" name="username"\nplaceholder='Username' size = '14'/><br/>\n\t\t<input type="password" name="password"  size = '14' placeholder='Password'/>\n\t\t<br/>\n\t\t<input type="submit" class="login" value="Sign in"/>\n</form>"""
	
	global challengerList
	challengerList = []
	responderList = []
	
	def initNoticeWindows(self):		
		self.challengeSentNotice = ''
		self.acceptChallengeOrNotNotice = ''
		self.respondNotice = ''
		self.challengeReceivedNotice = ''
		self.respondAccept = False
		self.showNotLoginNotice = False
		self.notLoginNotice = '\t\t\n\t\tfunction ChallengeSentAlert(msg,duration)\n\t\t{\n\t\t var styler = document.createElement("div");\n\t\t  styler.setAttribute("style","width:350px;height:50px; position:fixed; bottom:0;    right:0;background-color:#444;color:Silver;text-align: center;");\n\t\t styler.innerHTML = "<h4>"+msg+"</h4>";\n\t\t setTimeout(function()\n\t\t {\n\t\t   styler.parentNode.removeChild(styler);\n\t\t },duration);\n	\t document.body.appendChild(styler);\n\t\t}\n\t\t  function caller()\n	\t  {\n	\t\tChallengeSentAlert(" You have to login first to use this feature!","5000");\n\t\t  }\n\t\t'
	
	"""
	def encryptAES256(dataToEncrypt):
		BLOCK_SIZE = 16
		PADDING = ' '
		pad = lambda s : s + (BLOCK_SIZE - len(s) % BLOCK_SIZE) * PADDING
		encodeAES = lambda c, s: base64.b64encode(c.encrypt(pad(s)))
		secretStr = os.urandom(BLOCK_SIZE)
		print 'encryption key:', secretStr
		cipher = AES.new(secretStr)
		encodedData = encodeAES(cipher, dataToEncrypt)
		print 'Encrypted string: ', encodedData
		
	"""
	############ Database access functions declaration ############ 
	
	def createOnlinePlayerTable():
		# Create a query cursor on the database
		queryCurs = database.cursor()
		try:
			queryCurs.execute('''CREATE TABLE IF NOT EXISTS OnlinePlayers
			(id INTEGER PRIMARY KEY, username TEXT, location TEXT, ip TEXT, port TEXT, loginTime TEXT)''')
		except:
			# Return error code 1 if the table is failed to be created
			return 1
		database.commit()
		# Return error code 0 if the table has been created successully.
		return 0	
	
	# Log online user details to database, the input is a string array.
	def logOnlineUser(self, userDetails):
	
		username = userDetails[0] 
		location = userDetails[1]
		ip = userDetails[2]
		port = userDetails[3]
		loginTime = userDetails[4] 
		queryCurs.execute('''INSERT INTO OnlinePlayers (username, location, ip, port, loginTime)
		VALUES (?,?,?,?,?)''', (username, location, ip, port, loginTime))
		# Save the changes to the database
		database.commit()
		#print "logged " + username + " details to database!"
	
	# Update a specific online user details, where data is an array storing all the info
	# which including their username
	def updateUserDetails(self, data):
		username = data[0]
		data.remove(data[0])
		queryCurs.execute('''UPDATE OnlinePlayers
		SET location = ? , ip = ?, port = ?, loginTime = ?
		WHERE username = ?''', (data[0], data[1], data[2], data[3], username))
		# Save the changes to the database
		database.commit()
		#print "Updated user details in database!"
	
	# Delete a player who is offline
	def deletePlayer(self, username):
		queryCurs.execute('''DELETE FROM OnlinePlayers
		WHERE username = ?''', (username,))		
		# Save the changes to the database
		database.commit()
	
	# Get specific data of a player from database
	def getUserData(self, username, fieldToGet):
		queryCurs.execute('''SELECT * FROM OnlinePlayers
		WHERE username = ?''', (username,))		
		"""
		code for fieldToGet:
		id = 0, username = 1, location = 2, ip = 3, port = 4, loginTime = 5
		For fieldToGet = '*', this function will fetch all the fields of a
		player and returns as an string array if fieldToGet is '*', otherwise
		returns specific data according to fieldToGet.
		"""
		result = queryCurs.fetchone()
		if (fieldToGet == '*'):
			return result
		else:
			if (fieldToGet == 'id'):
				j = 0
			elif (fieldToGet == 'username'):
				j = 1
			elif (fieldToGet == 'location'):
				j = 2
			elif (fieldToGet == 'ip'):
				j = 3
			elif (fieldToGet == 'port'):
				j = 4
			elif (fieldToGet == 'loginTime'):
				j = 5
			return result[j]
	
	def checkUserRecordExist(self, username):
		queryCurs.execute('''SELECT * FROM OnlinePlayers
		WHERE username = ?''', (username,))
		result = queryCurs.fetchone()
		#print "result = " + str(result)
		if (result == None):
			# No record in database
			#print "No record in database!"
			return 0
		else:
			# There is a record in database
			#print "There is a record in database!"
			return 1
		
	############ End of Database functions declaration ############ 
	
	# Create a table to store the details of all online players
	onlinePlayerTableCheck = createOnlinePlayerTable()
	
	if (onlinePlayerTableCheck == 1):
		sys.exit("\nProgram fails to start since the database cannot be initialized to store online players information.")
	else:
		print "Database has been initialized successully!"
		
	#CherryPy Configuration
	_cp_config = {'tools.encode.on': True, 
				  'tools.encode.encoding': 'utf-8',
				  'tools.sessions.on' : 'True',
				 }
	
	# Open a browser to startup the multiplayer main page
	webbrowser.open_new('http://%s:%d/initialized' % (listen_ip, listen_port))
	
	#Ping from other ips (should work for school network and uni computer
	@cherrypy.expose
	def ping(self, sender=None):
		return '0'
		
	# If they try somewhere we don't know, catch it here and send them to the right place.
	@cherrypy.expose
	def default(self, *args, **kwargs):
		"""The default page, given when we don't recognise where the request is for."""
		Page = "404 Error. Since the request is not recognised by the server."
		cherrypy.response.status = 404
		return Page

	def checkNotification(self, Page):
		if (self.showNotLoginNotice == True):
			Page = Page.replace('<!--ALERT_POP_UP_WINDOW_PYTHON_VAR-->', self.notLoginNotice)
			self.showNotLoginNotice = False
		if (self.respondAccept == True):
			Page = Page.replace('<!--ALERT_POP_UP_WINDOW_PYTHON_VAR-->', self.respondNotice)
			self.respondNotice = ''
			self.respondAccept == False
			#cherrypy.HTTPRedirect('/combat')
		if (self.challengeSentNotice != ''):
			Page = Page.replace('<!--ALERT_POP_UP_WINDOW_PYTHON_VAR-->', self.challengeSentNotice)			
			self.challengeSentNotice = ''
		if (self.challengeReceivedNotice != ''):
			Page = Page.replace('<!--ALERT_POP_UP_WINDOW_PYTHON_VAR-->', self.
			challengeReceivedNotice)
			self.challengeReceivedNotice = ''
		return Page
		
	def checkLogin(self, Page):
		notLogin = False		
		try:
			username = cherrypy.session['username']
		except KeyError:
			notLogin = True
			
		if (notLogin):
			Page = Page.replace('<!-- LOGIN_FIELD_PYTHON_VAR-->', loginFieldHtml)
		else:			
			userInterface = '<div class="usernameText">Hello, ' + cherrypy.session['username'] + '<br>You are online!'
			userInterface += '\t\t<!--Logout button-->\n\t\t<form action="/signout" method="post" enctype="multipart/form-data">\n\t\t<input class="logout" type="submit" value="Logout"/></form></div>'
			userInterface += '\n\t\t<meta http-equiv="refresh" content="10" >'
			Page = Page.replace('<!-- LOGIN_FIELD_PYTHON_VAR-->', userInterface)
		return Page
		
	# PAGES (which return HTML that can be viewed in browser)
	
	# Initialixe notice windows and redirect the page to home
	@cherrypy.expose
	def initialized(self):		
		self.initNoticeWindows()
		raise cherrypy.HTTPRedirect('/index')
	
	@cherrypy.expose
	def index(self):
		openHtml = open('main.html', 'r')
		Page = openHtml.read()
		notLogin = False
		Page = self.checkLogin(Page)
		Page = self.checkNotification(Page)	
		return Page
	
	@cherrypy.expose
	def aboutUs(self):
		openHtml = open('aboutus.html', 'r')
		Page = str(openHtml.read())
		openHtml.close()
		Page = self.checkLogin(Page)
		Page = self.checkNotification(Page)
		return Page
	
	@cherrypy.expose
	def lobby(self):
		openHtml = open('lobby.html','r')
		Page = str(openHtml.read())
		openHtml.close()		
		playerOnlineDis = self.playerOnlineList()
		try:
			Page = Page.replace('<!--PLAYER_LIST_PYTHON_VAR-->', playerOnlineDis)
		except:
			# Reload the page
			time.sleep(10)
			raise cherrypy.HTTPRedirect('/lobby')
		Page = self.checkLogin(Page)
		Page = self.checkNotification(Page)
		return Page
	
		
	@cherrypy.expose
	def combat(self):
		
		
		return "Combat game."
		

	@cherrypy.expose	
	def sum(self, a=0, b=0): #All inputs are strings by default
		output = int(a)+int(b)
		return str(output) 
	
	@cherrypy.expose
	def listAPI(self):
		# Asking server for API list
		url= 'http://cs302.pythonanywhere.com/listAPI?'
		response_message = str((urllib2.urlopen(url)).read())
		response_token = response_message.split('/')
		Page = ""
		for i in range(len(response_token)):
			Page += response_token[i] + '<br>'
		# List API button
		Page += '<form action="/lobby" method="post" enctype="multipart/form-data">'
		Page += '<input type="submit" value="Home"/></form>'
		return Page

	@cherrypy.expose
	def playerOnlineList(self):
		""" to find how long a user has logged in:
		import time
		secSinceEpoch = time.time()
		howLong = secSinceEpoch - timeTheUserLoggedIn
		"""
		try:
			url= 'http://cs302.pythonanywhere.com/getList?username=' + str(cherrypy.session['username']) + '&password=' + str(cherrypy.session['password']) + '&enc=0'
		except:
			self.showNotLoginNotice = True
			raise cherrypy.HTTPRedirect('/')
		response_message = str((urllib2.urlopen(url)).read())
		print "Getting list of online user... "
		response = int(response_message[0])
		if (response == 0):
			response_message = response_message.replace("0, Online user list returned", "")
			realList = response_message
			Page = ''
			# Fetch online user list
			allUserDetail = response_message.split()
			for i in range(len(allUserDetail)):
				if ',' in allUserDetail[i]:
					splitDetail = allUserDetail[i].split(',')
					#for i in range(len(splitDetail)):
						#print splitDetail[i]
					for j in range(len(splitDetail)):
						if (len(splitDetail[j]) > 42):
							# Remove encrypted user from the list
							splitDetail.remove(splitDetail[j])
					if (splitDetail[0] != cherrypy.session['username']):
						username = splitDetail[0]
						# Update player's info if the player's record is in the database 
						if (self.checkUserRecordExist(splitDetail[0]) == 1):
							self.updateUserDetails(splitDetail)
						else:
							# Else store the user info to the database
							self.logOnlineUser(splitDetail)
						Page += """<div class = 'playersList'>"""
						Page += username +"<br> Network: " + self.getUserData(username, 'location') + '\n'
						Page += ' <form action="/challengePlayer?playerToBeChallenged=' + username + '" method="post" enctype="multipart/form-data">\n'
						Page += """<input class="challenge" onclick="javascript:displayTxt('show')" type="submit" value="Challenge"/></form>"""
						Page += '</div>\n'
					else:
						if (self.checkUserRecordExist(cherrypy.session['username']) == 1):
							self.updateUserDetails(splitDetail)
						else:							
							self.logOnlineUser(splitDetail)
			#Page += '<p>' + realList + '</p><br>'
			return Page
	
	# LOGGING IN AND OUT
	@cherrypy.expose
	def signin(self, username=None, password=None):
		"""Check their name and password and send them either to the main page, or back to the main login screen."""

		hashedPassword = hashlib.sha256(str(password+hash_salt)).hexdigest()
		self.autoReport = True
		error = self.authoriseUserLogin(username, hashedPassword)
		if (error == 0):
			# Activate report thread after user has logged in
			cherrypy.session['username'] = username
			cherrypy.session['password'] = hashedPassword
			self.tt = threading.Thread(target=self.report, args=[cherrypy.session['username'], cherrypy.session['password'], False])
			self.daemon = True
			self.tt.start()		
		else:		
			self.autoReport = False
		raise cherrypy.HTTPRedirect('/lobby')

	@cherrypy.expose
	def signout(self):
	
		"""Logs the current user out, expires their session"""
		username = cherrypy.session.get('username')
		if (username == None):
			Page = "You have not signed in.<br>Please click <a href='login'>here</a> to login."
			return Page
		else:
			print "Logging out... "
			
			try:
				url= 'http://cs302.pythonanywhere.com/logoff?username=' + str(cherrypy.session['username']) + '&password=' + str(cherrypy.session['password']) + '&enc=0'
			except ValueError:
				raise cherrypy.HTTPRedirect('/')
			
			self.autoReport = False
			response_message = (urllib2.urlopen(url)).read()
			response = str(response_message)[0]
			print "Server reponse: " + str(response_message)
			if (int(response) == 0):
				# Clear user session
				cherrypy.session.clear()   
				self.autoReport = False
				raise cherrypy.HTTPRedirect('/') 
			else:
				return "There is an error while logging out. Please try again later."

	# Reporting user online / login to server
	@cherrypy.expose
	def report(self, username, hashed_password, firstLogin):
		# Assume there is no error
		response = 0
		while (self.autoReport == True and int(response) == 0):
			location = self.findLocation()
			if (firstLogin == False):
				time.sleep(30)
			if (self.autoReport == True):
				try:
					url = 'http://cs302.pythonanywhere.com/report?username=' + str(username)
					url += '&password=' + str(hashed_password) + '&ip=' + listen_ip
					url += '&port=' + str(listen_port) + '&location=' + str(location) + '&enc=0'
				except ValueError:
					raise cherrypy.HTTPRedirect('/')
				# Getting the error code from the server
				response_message = (urllib2.urlopen(url)).read()
				response = str(response_message)[0]
				# Display response message from the server
				print "Server response: " + str(response_message)
				if (firstLogin == True):
					return int(response)
		return
		
	# Authorise user by sending login details to server
	def authoriseUserLogin(self, username, hashed_password):	   
		print "Logging in as " + username + " ..."
		response = self.report(username, hashed_password, True)
		return response
	
	# A function which allow this player to challenge to other player online
	# This computer will be the master if this function runs
	@cherrypy.expose
	def challengePlayer(self, playerToBeChallenged):
		try:
			username = cherrypy.session['username']
		except:
			self.showNotLoginNotice = True
			raise cherrypy.HTTPRedirect('/')	
		print "challenging sender: " + playerToBeChallenged 
		userIP = self.getUserData(playerToBeChallenged, 'ip')
		userPort = self.getUserData(playerToBeChallenged, 'port')
		userNetwork = self.getUserData(playerToBeChallenged, 'location')
		ownNetwork = self.getUserData(cherrypy.session['username'], 'location')
		print "own network: " + ownNetwork
		print "user network: " + userNetwork
		# Pop up a message if the network location is not the same
		if (ownNetwork != userNetwork):
			message = "Cannot connect to this player as your location is not the same as this player. :("
			self.challengeSentNotice = '\t\t\n\t\tfunction ChallengeSentAlert(msg,duration)\n\t\t{\n\t\t var styler = document.createElement("div");\n\t\t  styler.setAttribute("style","width:350px;height:50px; position:fixed; bottom:0;    right:0;background-color:#444;color:Silver;text-align: center;");\n\t\t styler.innerHTML = "<h4>"+msg+"</h4>";\n\t\t setTimeout(function()\n\t\t {\n\t\t   styler.parentNode.removeChild(styler);\n\t\t },duration);\n	\t document.body.appendChild(styler);\n\t\t}\n\t\t  function caller()\n	\t  {\n	\t\tChallengeSentAlert("' + message + '","5000");\n\t\t  }\n\t\t'
			raise cherrypy.HTTPRedirect('/lobby')
			return
		response = 1
		try:
			# Test if they are actually online
			response = urllib2.urlopen('http://%s:%s/ping?sender=%s' % \
						(userIP, userPort, cherrypy.session['username']), timeout=30)
			response = int(response.read())
		except:
			if (response != 0):
				message = "Cannot connect to this player. :("
				self.challengeSentNotice = '\t\t\n\t\tfunction ChallengeSentAlert(msg,duration)\n\t\t{\n\t\t var styler = document.createElement("div");\n\t\t  styler.setAttribute("style","width:350px;height:50px; position:fixed; bottom:0;    right:0;background-color:#444;color:Silver;text-align: center;");\n\t\t styler.innerHTML = "<h4>"+msg+"</h4>";\n\t\t setTimeout(function()\n\t\t {\n\t\t   styler.parentNode.removeChild(styler);\n\t\t },duration);\n	\t document.body.appendChild(styler);\n\t\t}\n\t\t  function caller()\n	\t  {\n	\t\tChallengeSentAlert("' + message + '","5000");\n\t\t  }\n\t\t'					
			raise cherrypy.HTTPRedirect('/lobby')
			return	
		if (response == 0):
			print playerToBeChallenged + " is online!"
			print "port: " + userPort + "   userIP: " + userIP
			username = cherrypy.session['username']
			sendingData = {"sender": username}
			json_data = json.dumps(sendingData)
			response = 1
			try:
				req = urllib2.Request('http://%s:%s/challenge?' % \
						(userIP, userPort), json_data, {'Content-Type': 'application/json'})
				response = urllib2.urlopen(req, timeout=30)
				response = int(response.read())
				print "what is response: " + str(response)
			except:
				pass
			if (response != 0):
				message = "Cannot connect to this player. :("
				self.challengeSentNotice = '\t\t\n\t\tfunction ChallengeSentAlert(msg,duration)\n\t\t{\n\t\t var styler = document.createElement("div");\n\t\t  styler.setAttribute("style","width:350px;height:50px; position:fixed; bottom:0;    right:0;background-color:#444;color:Silver;text-align: center;");\n\t\t styler.innerHTML = "<h4>"+msg+"</h4>";\n\t\t setTimeout(function()\n\t\t {\n\t\t   styler.parentNode.removeChild(styler);\n\t\t },duration);\n	\t document.body.appendChild(styler);\n\t\t}\n\t\t  function caller()\n	\t  {\n	\t\tChallengeSentAlert("' + message + '","5000");\n\t\t  }\n\t\t'
				raise cherrypy.HTTPRedirect('/lobby')
				return
			openHtml = open('lobby.html', 'r')
			Page = openHtml.read()
			self.challengeSentNotice = ''
			if (response == 0):
				# The return statement has not been tested
				print "Challenge has been sent!"
				openStr = "'open'"
				closeStr = "'close'"
				message = "Challenge has been sent to " + playerToBeChallenged + '!'
			# Javascript which will pop up an alert message for 3 seconds
			self.challengeSentNotice = '\t\t\n\t\tfunction ChallengeSentAlert(msg,duration)\n\t\t{\n\t\t var styler = document.createElement("div");\n\t\t  styler.setAttribute("style","width:350px;height:50px; position:fixed; bottom:0;    right:0;background-color:#444;color:Silver;text-align: center;");\n\t\t styler.innerHTML = "<h4>"+msg+"</h4>";\n\t\t setTimeout(function()\n\t\t {\n\t\t   styler.parentNode.removeChild(styler);\n\t\t },duration);\n	\t document.body.appendChild(styler);\n\t\t}\n\t\t  function caller()\n	\t  {\n	\t\tChallengeSentAlert("' + message + '","5000");\n\t\t  }\n\t\t'		
			print"hihihiii"
			raise cherrypy.HTTPRedirect('/lobby')
			
	# This computer will be the slave if this function runs
	@cherrypy.expose
	def respondToChallenge(self, accept, sender):
		accept = int(accept)
		userIP = self.getUserData(sender, 'ip')
		userPort = self.getUserData(sender, 'port')
		sendingData = { 'sender' : cherrypy.session['username'], 'accept' : accept }
		# Json encode the sending data
		json_data = json.dumps(sendingData)
		req = urllib2.Request('http://%s:%s/respond?' % \
					(userIP, userPort), json_data, {'Content-Type': 'application/json'})							
		response = urllib2.urlopen(req)
		response = response.read()
		print "response: " + str(response)
		if (int(response) == 0):
			respondNotice = "Successfully respond to the challenge from %s ." % (sender)
			slave = True
			challengerList.remove(sender)
			raise cherrypy.HTTPRedirect('/listChallenger')
		else:
			print "Fail to respond."
			
	# Allow other player online to invite this computer for a challenge 
	@cherrypy.expose
	# Cherrypy auto-json decode declaration
	@cherrypy.tools.json_in()
	def challenge(self):
		# Receive the Json object sent from the other player's computer
		received_data = cherrypy.request.json
		challenger = received_data['sender']
		if challenger not in challengerList:
			challengerList.append(challenger)
		userIP = self.getUserData(challenger, 'ip')
		message = "A challenge request is received from " + challenger + " !"
		self.challengeReceivedNotice = '\t\t\n\t\tfunction ChallengeSentAlert(msg,duration)\n\t\t{\n\t\t var styler = document.createElement("div");\n\t\t  styler.setAttribute("style","width:350px;height:50px; position:fixed; bottom:0;    right:0;background-color:#444;color:Silver;text-align: center;");\n\t\t styler.innerHTML = "<h4>"+msg+"</h4>";\n\t\t setTimeout(function()\n\t\t {\n\t\t   styler.parentNode.removeChild(styler);\n\t\t },duration);\n	\t document.body.appendChild(styler);\n\t\t}\n\t\t  function caller()\n	\t  {\n	\t\tChallengeSentAlert("' + message + '","5000");\n\t\t  }\n\t\t'	
		# Tell the challenger that the invitation has been received
		# Only returning error code does not require Json ecoding
		return '0'

	# Allow other player online to respond the challenge sent by this player
	@cherrypy.expose
	@cherrypy.tools.json_in()
	def respond(self):
		received_data = cherrypy.request.json
		responder = received_data['sender']
		accept = received_data['accept']		
		if (int(accept) == 1):
			self.respondAccept = True
			message = "Your challenge to " + str(responder) + " is accepted!\n"
			master = True
			message += "Loading game files......"
			timeSec = '20000'
		else:
			self.respondAccept = True
			message = responder + " has declined your challenge invitation."
			timeSec = '5000'	
		self.respondNotice = '\t\t\n\t\tfunction respondReceivedAlert(msg,duration)\n\t\t{\n\t\t var styler = document.createElement("div");\n\t\t  styler.setAttribute("style","width:350px;height:50px; position:fixed; bottom:0;    right:0;background-color:#444;color:Silver;text-align: center;");\n\t\t styler.innerHTML = "<h4>"+msg+"</h4>";\n\t\t setTimeout(function()\n\t\t {\n\t\t   styler.parentNode.removeChild(styler);\n\t\t },duration);\n	\t document.body.appendChild(styler);\n\t\t}\n\t\t  function caller()\n	\t  {\n	\t\trespondReceivedAlert("' + message + '","'+ timeSec +'");\n\t\t  }\n\t\t'
		return '0'
		
	@cherrypy.expose
	def listChallenger(self):
		try:
			username = cherrypy.session['username']
		except:
			self.showNotLoginNotice = True
			raise cherrypy.HTTPRedirect('/')
		openHtml = open('challenger.html', 'r')
		Page = openHtml.read()
		openHtml.close()
		challengerDisplay = ''
		Page = self.checkLogin(Page)
		Page = self.checkNotification(Page)
		# Nothing will display if the challenger list is empty
		print "challengerlist number: " + str(len(challengerList))
		if (len(challengerList) == 0):
			challengerDisplay = "You do not have any challenge request. Go to find someone and challenge them!"
		else:
			for i in range(len(challengerList)):
				challengerDisplay += '<br>' + challengerList[i]
				challengerDisplay += '<form action="/respondToChallenge?accept=1&sender='+ challengerList[i] +'" method="post" enctype="multipart/form-data">\n'
				challengerDisplay += '<input type="submit" class="challenge" value="Accept"/></form>\n\t\t'
				challengerDisplay += '<form action="/respondToChallenge?accept=0&sender='+ challengerList[i] + '" method="post" enctype="multipart/form-data">\n'
				challengerDisplay += '<input type="submit" class="challenge" value="Decline"/></form>'
				challengerDisplay += '<form action="/removeChallenger?challengerName='+ challengerList[i] +'" method="post" enctype="multipart/form-data">\n'
				challengerDisplay += '<input type="submit" class="challenge" value="Delete"/></form>'
		Page = Page.replace('<!-- CHALLENGER_LIST_PYTHON_VAR -->', challengerDisplay)
		return Page
	
	@cherrypy.expose
	def removeChallenger(self, challengerName):
		challengerList.remove(challengerName)
		raise cherrypy.HTTPRedirect('/listChallenger')
	
	# Determine what kind of network is this computer using to connect to the server
	def findLocation(self):
		# According to https://www.sit.auckland.ac.nz/Network:_University_of_Auckland_IP_addresses
		# Univeristy Lab Desktop - 0
		try:
			position = listen_ip.index('10.103')
		except ValueError:
			position = -1
		
		if (position != -1):
			return 0
		
		try:
			position = listen_ip.index('10.104')
		except ValueError:
			position = -1
		
		if (position != -1):
			return 0
		
		try:
			position = listen_ip.index('130.216')
		except ValueError:
			position = -1
		
		if (position != -1):
			return 0
		# Univeristy Wireless Network - 1
		try:
			position = listen_ip.index('172.23')
		except ValueError:
			position = -1
		
		if (position != -1):
			return 1
		
		try:
			position = listen_ip.index('172.24')
		except ValueError:
			position = -1
		
		if (position != -1):
			return 1
		
		try:
			position = listen_ip.index('172.18')
		except ValueError:
			position = -1
		
		if (position != -1):
			return 1
		# Rest of the world - 2
		return 2
	
def runMainApp():
	# Create an instance of MainApp and tell Cherrypy to send all requests under / to it. (ie all of them)
	
	config = {
         '/': {
             'tools.sessions.on': True,
             'tools.staticdir.root': os.path.abspath(os.getcwd())
         },
         '/generator': {
             'request.dispatch': cherrypy.dispatch.MethodDispatcher(),
             'tools.response_headers.on': True,
             'tools.response_headers.headers': [('Content-Type', 'text/plain')],
         },
         '/static': {
             'tools.staticdir.on': True,
             'tools.staticdir.dir': './public'
         }
	}
	
	cherrypy.tree.mount(MainApp(), "/", config)

	# Tell Cherrypy to listen for connections on the configured address and port.
	cherrypy.config.update({'server.socket_host': listen_ip,
							'server.socket_port': listen_port,
							'engine.autoreload.on': True,
						   })

	print "\n========================="
	print "University of Auckland"
	print "COMPSYS302 - Software Design Application"
	print "========================================"					   
	
	# Start the web server
	cherrypy.engine.start()

	# And stop doing anything else. Let the web server take over.
	cherrypy.engine.block()
 
#Run the function to start everything
runMainApp()
