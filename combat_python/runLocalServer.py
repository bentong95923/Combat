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

#listen_ip = str(socket.gethostbyname(socket.getfqdn()))
listen_ip = str(socket.gethostbyname(socket.gethostname()))
listen_port = 10001
hash_salt = "COMPSYS302-2016"

reportToServer = False

# Dictionaries

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
        except KeyError: #There is no username
            Page += "Click here to <a href='login'>login</a>."
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
    def report(self):
        self.reporting()
        # Reporting to the server at least 30 secs everytime
        reportTimer = threading.Timer(30,self.reporting)
        if (reportToServer == True):
            reportTimer.start()
        raise cherrypy.HTTPRedirect('/')

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
            # Activate report timer after user has logged in
            reportToServer = True
            cherrypy.session['username'] = username;
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
            cherrypy.lib.sessions.expire()
        raise cherrypy.HTTPRedirect('/')
        
    # Reporting user online
    @cherrypy.expose
    def reporting(self):
        response = urllib2.urlopen('http://andrewchenuoa.pythonanywhere.com/report?username=dkwe876&password=5400e1ed703ee71eee6e22bd3ae47d64b6a1626dfa8f860888ebe652f23086c9&location=0&ip='+ str(listen_ip) + '&port=10001&pubkey=0&enc=0')
        variable = response.read()
        print variable
        
    def authoriseUserLogin(self, username, hashed_password):
        # Rmove them after testing
        print "logging in as " + username + " ..."
        #
        location = self.findLocation()
        url= 'http://cs302.pythonanywhere.com/report?username=' + str(username) + '&password=' + str(hashed_password) + '&ip=' + listen_ip + '&port=' + str(listen_port) + '&location=' + str(location) + '&enc=0'

        # Getting the error code from the server
        responseObj = (urllib2.urlopen(url)).read()
        response = str(responseObj)[0]
        print "response: " + str(responseObj)
        """if (username.lower() == "ben") and (password.lower() == "password"):
            return 0
        else:
            return 1"""
        
        return int(response)
    
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
