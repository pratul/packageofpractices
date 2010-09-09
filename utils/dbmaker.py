#!/usr/bin/env python
#
# Copyright 2010 (c) Pratul Kalia


import xmlrpclib, sys

XMLRPC_SERVER = "http://staging/services/xmlrpc"

drupal = xmlrpclib.ServerProxy(XMLRPC_SERVER)
drupal.system.connect()
nids = drupal.agro.getpopnids()
for n in nids:
    print drupal.node.get(n, ['nid', 'title'])
