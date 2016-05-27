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
import threading

def getIPAddress():
    try:
    	# Try to get uni internal IP address
        IPAddress = socket.gethostbyname(socket.getfqdn())
    except:
    	# If not successful then try to get the local or external IP address
        IPAddress = socket.gethostbyname(socket.gethostname())
    return IPAddress

listen_ip = str(getIPAddress())
listen_port = 10001
hashed_pass = ''
hash_salt = "COMPSYS302-2016"
    
# Dictionaries

reportToServer = False


class MainApp(object):

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
        	Page += "Click here to <a href='login'>login</a>."
        if (loggedin == True):
            Page += '<form action="/signout" method="post" enctype="multipart/form-data">'
            Page += '<input type="submit" value="Logout"/></form>'
        return Page

    @cherrypy.expose

    def login(self):
        Page = '<form action="/signin" method="post" enctype="multipart/form-data">'
        Page += 'Username: <input type="text" name="username"/><br/>'
        Page += 'Password: <input type="text" name="password"/>'
        Page += '<input type="submit" value="Login"/></form>'
        return Page
    
    # Report the user to server every 30secs
    @cherrypy.expose
    def report(self, username, password):
        response = self.reporting(username, password)
        # Reporting to the server at least 30 secs everytime
        reportTimer = threading.Timer(30,self.reporting)
        if (reportToServer == True):
            reportTimer.start()
        return response
        
    @cherrypy.expose    
    def sum(self, a=0, b=0): #All inputs are strings by default
        output = int(a)+int(b)
        return str(output)

    # LOGGING IN AND OUT
    @cherrypy.expose
    def signin(self, username=None, password=None):
        """Check their name and password and send them either to the main page, or back to the main login screen."""

        hashedPassword = hashlib.sha256(str(password+hash_salt)).hexdigest()
        error = self.authoriseUserLogin(username,hashedPassword)
        if (error == 0):
            # Activate report thread after user has logged in
            reportToServer = True
            cherrypy.session['username'] = username
            cherrypy.session['password'] = hashedPassword
            raise cherrypy.HTTPRedirect('/')
            
        else:
            raise cherrypy.HTTPRedirect('/login')

    @cherrypy.expose
    def signout(self):
        """Logs the current user out, expires their session"""
        username = cherrypy.session.get('username')
        if (username == None):
            pass
        else:
            url= 'http://cs302.pythonanywhere.com/logoff?username=' + str(cherrypy.session['username']) + '&password=' + str(cherrypy.session['password']) + '&enc=0'
            responseObj = (urllib2.urlopen(url)).read()
            response = str(responseObj)[0]
            print "Server reponse: " + str(responseObj)
            if (int(response) == 0):
                # Clear user session 
                cherrypy.session.clear()
                raise cherrypy.HTTPRedirect('/') 
            else:  
                return "There is an error while logging out."
            
    # Reporting user online / login to server
    @cherrypy.expose
    def reporting(self, username, hashed_password):
        location = self.findLocation()
        url= 'http://cs302.pythonanywhere.com/report?username=' + str(username) + '&password=' + str(hashed_password) + '&ip=' + listen_ip + '&port=' + str(listen_port) + '&location=' + str(location) + '&enc=0'
        # Getting the error code from the server
        responseObj = (urllib2.urlopen(url)).read()
        response = str(responseObj)[0]
        # Display response from the server
        print "Server response: " + str(responseObj)
        return int(response)
        
    # Authorise user by sending login details to server
    def authoriseUserLogin(self, username, hashed_password):        
        print "logging in as " + username + " ..."        
        response = self.report(username, hashed_password)        
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

    print "========================="
    print "University of Auckland"
    print "COMPSYS302 - Software Design Application"
    print "========================================"                       
    
    # Start the web server
    cherrypy.engine.start()

    # And stop doing anything else. Let the web server take over.
    cherrypy.engine.block()
 
#Run the function to start everything
runMainApp()
