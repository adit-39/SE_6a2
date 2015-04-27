#!/usr/bin/python
import sys
import logging
logging.basicConfig(stream=sys.stderr)
sys.path.insert(0,"/var/www/FlaskApp1/")

from FlaskApp1 import app as application
application.secret_key = 'AIzaSyA_dwgg9ubyOxOb8SR8V9-kmdkuFKQvCRc'
