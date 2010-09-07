#!/usr/bin/env python
#
# Copyright 2010 (c) Pratul Kalia


import xmlrpclib, sys

XMLRPC_SERVER = "http://staging?q=services/xmlrpc"

drupal = xmlrpclib.ServerProxy(XMLRPC_SERVER)
drupal.system.connect()
print drupal.agro.getpopnids()
