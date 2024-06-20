#!/bin/bash

#In most cases we assume that migration was successful (we don't have to repeat it).
#Even if there is no file dictionary or the destination dictionary already exists we assume that the result is SUCCESS and we can safely use DB DictionarySource
MigrateDictionary -dst DB

exit $?
