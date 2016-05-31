#!/usr/bin/python
""" runLocalServer.py

    COMPSYS302 - Software Design
    Author: Andrew Chen (andrew.chen@auckland.ac.nz)
    Last Edited: 19/02/2015

    This program uses the CherryPy web server (from www.cherrypy.org).
"""
# Requires:  CherryPy 3.2.2  (www.cherrypy.org)
#            Python  (We use 2.7)

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
from threading import Timer

def getIPAddress():
    try:
    	# Try to get uni internal IP address
        IPAddress = socket.gethostbyname(socket.getfqdn())
    except:
    	# If not successful then try to get the local or external IP address
        IPAddress = socket.gethostbyname(socket.gethostname())
    return IPAddress

listen_ip = str(getIPAddress())
listen_port = 12121
hashed_pass = ''
hash_salt = "COMPSYS302-2016"
databaseFileName = 'combatDatabase.db'

class MainApp(object):

    def createTable():
        try:
            queryCurs.execute('''CREATE TABLE IF NOT EXISTS onlinePlayers
            (id INTEGER PRIMARY KEY, username TEXT, location INTEGER, ip TEXT, port INTEGER, loginTime REAL)''')
        except:
            return 0
        return 1
        
    def logOnlineUser(self, username, location, ip, port, loginTime):
        queryCurs.execute('''INSERT INTO onlinePlayers (username, location, ip, port, loginTime)
        VALUES (?,?,?,?,?)''', (username, location, ip, port, loginTime))
    
    # Create a database for the game
    database = sqlite3.connect(databaseFileName)
    # Create a database cursor to implement database enquiries
    queryCurs = database.cursor()
    # Create a table to store the details of all online players
    tableCheck = createTable()
    
    if (tableCheck == 1):
        sys.exit("Program fails to create a table to store online player information.")
        
    #CherryPy Configuration
    _cp_config = {'tools.encode.on': True, 
                  'tools.encode.encoding': 'utf-8',
                  'tools.sessions.on' : 'True',
                 }
    
    # If they try somewhere we don't know, catch it here and send them to the right place.
    @cherrypy.expose
    def default(self, *args, **kwargs):
        """The default page, given when we don't recognise where the request is for."""
        Page = "404 Error. Since the request is not recognised by the server."
        cherrypy.response.status = 404
        return Page

    # PAGES (which return HTML that can be viewed in browser)
    @cherrypy.expose
    def index(self):
        Page = "Welcome! This is a test website for COMPSYS302!<br/>"
        
        try:
            Page += "Hello " + cherrypy.session['username'] + "!"
            loggedin = True
        except KeyError: #There is no username
        	loggedin = False
        	Page += "Click <a href='login'>here</a> to login."
        if (loggedin == True):
            # Logout button
            Page += '<form action="/signout" method="post" enctype="multipart/form-data">'
            Page += '<input type="submit" value="Logout"/></form>'
            # List API button
            Page += '<form action="/listAPI" method="post" enctype="multipart/form-data">'
            Page += '<input type="submit" value="List API"/></form>'
            # List getList button
            Page += '<form action="/getList" method="post" enctype="multipart/form-data">'
            Page += '<input type="submit" value="Online user list"/></form>'
            
        return Page

    @cherrypy.expose
    def login(self):
        Page = '<form action="/signin" method="post" enctype="multipart/form-data">'
        Page += 'Username: <input type="text" name="username"/><br/>'
        Page += 'Password: <input type="password" name="password"/>'
        Page += '<input type="submit" value="Login"/></form>'
        return Page
    
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
        Page += '<form action="/index" method="post" enctype="multipart/form-data">'
        Page += '<input type="submit" value="Home"/></form>'
        return Page

    @cherrypy.expose
    def getList(self):
        """ to find how long a user has logged in:
        import time
        secSinceEpoch = time.time()
        howLong = secSinceEpoch - timeTheUserLoggedIn
        """
        try:
            url= 'http://cs302.pythonanywhere.com/getList?username=' + str(cherrypy.session['username']) + '&password=' + str(cherrypy.session['password']) + '&enc=0'
        except ValueError:
            raise cherrypy.HTTPRedirect('/')
        response_message = str((urllib2.urlopen(url)).read())
        print "Getting list of online user... "
        response = int(response_message[0])
        if (response == 0):
            response_message = response_message.replace("0, Online user list returned", "")
            message_split = response_message.split()
            
            Page = "User online: <br>"
            Page = response_message + '<br>'
            """
            finishReading = False
            while (finishReading == False):
                
            """
            Page += '<form action="/index" method="post" enctype="multipart/form-data">'
            Page += '<input type="submit" value="Home"/></form>'
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
            raise cherrypy.HTTPRedirect('/')            
        else:        
            self.autoReport = False
            raise cherrypy.HTTPRedirect('/login')

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
                
                self.autoReport = True
                return "There is an error while logging out. Please try again later."

    # Send a challenge to a player online
    @cherrypy.expose
    def challenge(sender):
        receiverOnline = urllib2.urlopen('http://' + str(reciverIP) + ':'  + str(receiverPort) + '/ping?')        
        if (receiverOnline == 0):
            sentChallenge = urllib2.urlopen('http://' + str(reciverIP) + ':' + str(receiverPort) + '/challenge?sender=' + str(receiverName))
            if (sentChallenge == 0):
                message = 'Challenge sent!'
            
            elif (sentCallenge == 3):
                message = 'Player is not currently available.'
            # Pop up message to the broswer
            return '<script> function myFunction() {alert("' + message + '");}</script>'

    """
    # Response a challenge after a challenge request has been received
    @cherrypy.expose
    def respond(senderIP, senderName, senderPort):
        response = urllib2.urlopen('http://' + str(senderIP) + ':' + str(senderPort) + '/challenge?respond=' + str(senderName) + )
        if (response = )
    """
    
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
                    url = 'http://cs302.pythonanywhere.com/report?username=' + str(username) + '&password=' + str(hashed_password) + '&ip=' + listen_ip + '&port=' + str(listen_port) + '&location=' + str(location) + '&enc=0'
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
    cherrypy.tree.mount(MainApp(), "/")

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
