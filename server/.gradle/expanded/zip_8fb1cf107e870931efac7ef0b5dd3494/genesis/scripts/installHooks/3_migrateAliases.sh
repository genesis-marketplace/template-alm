#!/bin/bash

#In most cases we assume that migration was successful (we don't have to repeat it).
#Even if there is no file alias store or the destination alias store already exists we assume that the result is SUCCESS and we can safely use DB AliasSource
MigrateAliases -dst DB

exit $?
