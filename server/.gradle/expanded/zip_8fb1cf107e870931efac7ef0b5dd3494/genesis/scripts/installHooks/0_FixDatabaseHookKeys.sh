#!/usr/bin/env bash

source $HOME/.bashrc
shopt -s expand_aliases

JvmRun global.genesis.environment.scripts.FixDatabaseHookKeys

exit $?