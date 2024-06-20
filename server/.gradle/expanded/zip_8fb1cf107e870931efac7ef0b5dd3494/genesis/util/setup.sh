#!/bin/bash
#
#   System              : DTA Utilities
#   Sub-System          : DTA Configuration
#   Version             : 1.0
#   Copyright           : (c) GENESIS
#   Date                : 14th August 2014
#   Author              : James Harrison
#
#   Function : Entry point for BASH DTA settings.
#
#   Modification History
#

# ------------------------------------------------------------------------------
# Setup the DTA environment
# ------------------------------------------------------------------------------
case "$OSTYPE" in
  msys*)    export GENESIS_OS="windows" ;;
  linux*)   export GENESIS_OS="unix" ;;
  darwin*)   export GENESIS_OS="unix" ;;
  *)        echo "WARNING: Assuming unix since \$OSTYPE is unknown: $OSTYPE";export GENESIS_OS="unix" ;;
esac

# ------------------------------------------------------------------------------
# Set the python path for module loading
# ------------------------------------------------------------------------------
export PYTHONPATH=$GENESIS_HOME/site-specific/scripts/pymodules

export PYTHONPATH=$PYTHONPATH:$GENESIS_HOME/genesis/scripts/$GENESIS_OS/python3/pymodules
export PYTHONPATH=$PYTHONPATH:$GENESIS_HOME/genesis/scripts/$GENESIS_OS/python3

# Set standard directories
# ------------------------------------------------------------------------------
export L=$GENESIS_HOME/runtime/logs
export GC=$GENESIS_HOME/generated/cfg
export SC=$GENESIS_HOME/site-specific/cfg
export SS=$GENESIS_HOME/site-specific/scripts

# ------------------------------------------------------------------------------
# set path to script directories so they can be called from anywhere
# ------------------------------------------------------------------------------
export PATH=$PATH:$GENESIS_HOME/site-specific/scripts:$GENESIS_HOME/genesis/scripts/$GENESIS_OS
export PATH=$PATH:$GENESIS_HOME/genesis/scripts/$GENESIS_OS/python3

alias h='history'

# Newly migrated bash scripts to new JvmRun approach
alias AeronArchive='JvmRun -modules=genesis-cluster global.genesis.cluster.script.AeronScriptTerminal'
alias AeronStat='JvmRun -modules=genesis-cluster global.genesis.cluster.stats.AeronStat'
alias cfgChecker='JvmRun -modules=genesis-cfgchecker global.genesis.cfgchecker.CfgChecker'
alias DbMon='JvmRun -modules=genesis-dbmon global.genesis.dbmon.DbMon'
alias encryptUserPass='JvmRun -modules=genesis-genesistodb global.genesis.genesistodb.utils.PasswordEncryptor'
alias encryptUserPassWithKey='JvmRun -modules=genesis-config global.genesis.config.utils.PasswordEncryptor'
alias loadData='JvmRun global.genesis.environment.scripts.BulkLoader'
alias productGen='JvmRun global.genesis.environment.productgen.ProductGen'
alias readSysDef='JvmRun global.genesis.environment.scripts.sysdef.ReadSysDefKt'

# Newly migrated old Groovy scripts approach to new direct main class approach when possible.
alias AppGen='JvmRun global.genesis.environment.scripts.euc.AppGenKt'
alias CountRecords='JvmRun global.genesis.environment.scripts.CountRecords'
alias CreateMissingSqlSequences='JvmRun global.genesis.environment.scripts.CreateMissingSqlSequences'
alias CSVReport='JvmRun global.genesis.environment.scripts.CSVReport'
alias DataserverToDataserver2Migration='JvmRun -modules=genesis-dataserver2,genesis-clustersupport global.genesis.dataserver2.script.DataserverToDataserver2MigrationKt'
alias deleteOrphanRecords='JvmRun global.genesis.environment.scripts.DeleteDependentRecordsWithOptions'
alias DictionaryBuilder='JvmRun -modules=genesis-dictionarybuilder global.genesis.dictionarybuilder.DictionaryBuilder'
alias DropTable='JvmRun global.genesis.environment.scripts.DropTable'
alias DumpAuthTable='JvmRun global.genesis.environment.scripts.DumpAuthTable'
alias DumpStrCliTable='JvmRun -modules=genesis-pal-streamerclient,genesis-environment global.genesis.streamerclient.pal.script.DumpStrCliTable'
alias ClearStrCliSourceRef='JvmRun -modules=genesis-pal-streamerclient,genesis-environment global.genesis.streamerclient.pal.script.ClearStrCliSourceRef'

alias DumpChronicleQueue='JvmRun net.openhft.chronicle.queue.DumpQueueMain'
alias DumpIt='JvmRun global.genesis.environment.scripts.DumpTable'
alias ExcelToGenesis='JvmRun global.genesis.environment.scripts.euc.ExcelToGenesisKt'
alias GenerateMetricsReport='JvmRun -modules=genesis-metrics global.genesis.metrics.scripts.ReadMetricLog'
alias generateSQLFromRDB='JvmRun -modules=genesis-dbtogenesis,genesis-clustersupport global.genesis.dbtogenesis.gen.GenerateDbToGenesisSQL'
alias generateSQLToRDB='JvmRun -modules=genesis-genesistodb,genesis-clustersupport global.genesis.genesistodb.gen.GenerateGenesisToDbSQL'
alias GenesisLoadTester='JvmRun -modules=genesis-web-client-api global.genesis.websocket.GenesisLoadTester'
alias ReconcileDatabaseSync='JvmRun -modules=genesis-sync global.genesis.sync.scripts.ReconcileDatabaseSync'
alias GetAutoIncrementCount='JvmRun global.genesis.environment.scripts.GetAutoIncrementCount'
alias GetNextSequenceNumbers='JvmRun global.genesis.environment.scripts.GetNextSequenceNumbers'
alias GetSequenceCount='JvmRun global.genesis.environment.scripts.GetSequenceCount'
alias HostDiff='JvmRun global.genesis.environment.scripts.clusterscripts.HostDiff'
alias LogLevel='JvmRun global.genesis.environment.scripts.SetLogLevel'
alias MigrateDictionaryToPAL='JvmRun global.genesis.environment.scripts.pal.dictionary.DictionaryMigrationKt'
alias MigrateInstallHooks='JvmRun global.genesis.environment.scripts.MigrateInstallHooks'
alias MigrateSysDefToPAL='JvmRun global.genesis.environment.scripts.pal.sysdef.SysDefMigrationKt'
alias MonCluster='JvmRun global.genesis.environment.scripts.clusterscripts.MonCluster'
alias PopulateHolidays='JvmRun global.genesis.environment.scripts.PopulateHolidays'
alias PurgeHftTables='JvmRun global.genesis.environment.scripts.purgescripts.PurgeHftTables'
alias PurgeTables='JvmRun global.genesis.environment.scripts.purgescripts.PurgeTables'
alias RecoverAuthTable='JvmRun global.genesis.environment.scripts.RecoverAuthTable'
alias RecoverHFTTable='JvmRun global.genesis.environment.scripts.RecoverHFTTable'
alias RenameFields='JvmRun global.genesis.environment.scripts.renameFields.RenameFieldsTool'
alias SendAuthTable='JvmRun global.genesis.environment.scripts.SendAuthTable'
alias SendIt='JvmRun global.genesis.environment.scripts.SendTable'
alias SetAutoIncrement='JvmRun global.genesis.environment.scripts.SetAutoIncrement'
alias SetPrimary='JvmRun global.genesis.environment.scripts.clusterscripts.SetPrimary'
alias SetProcessState='JvmRun global.genesis.environment.scripts.SetProcessState'
alias SetSequence='JvmRun global.genesis.environment.scripts.SetSequence'
alias UnsetPrimary='JvmRun global.genesis.environment.scripts.clusterscripts.UnsetPrimary'
alias JsonToKtsDictionary='JvmRun global.genesis.environment.scripts.JsonToKtsDictionary'
alias CreateProductProcessFiles='JvmRun global.genesis.environment.scripts.CreateProductProcessFiles'
alias JsonToKtsStateEventHandler='JvmRun global.genesis.environment.scripts.JsonToKtsStateEventHandlerKt'

alias ClearCodegenCache='JvmRun -modules=genesis-environment global.genesis.environment.scripts.ClearCodegenCache'
alias GenesisShell='JvmRun -modules=genesis-shell global.genesis.shell.GenesisShell'
alias KtsToJsonDictionary='JvmRun global.genesis.environment.scripts.KtsToJsonDictionary'
alias GenerateProject='JvmRun -modules=genesis-project-templates global.genesis.template.cli.GenesisServerGeneratorCLI'
alias MigrateFdbCounter='JvmRun global.genesis.environment.scripts.MigrateFdbCounterKt'
alias FDBKeyValueCount='JvmRun global.genesis.environment.scripts.FDBKeyValueCount'
alias RefreshTokenCleanUp='JvmRun global.genesis.environment.scripts.RefreshTokenCleanUp'
alias SetOrphanedAutoIncrementedFields='JvmRun global.genesis.environment.scripts.SetOrphanedAutoIncrementedFields'
alias FixEnumValues='JvmRun global.genesis.environment.scripts.fixEnumValues.FixEnumValues'
alias FindFieldsAboveMaxSize='JvmRun global.genesis.environment.scripts.FindFieldsAboveMaxSize'
alias ClasspathGenerator='JvmRun global.genesis.environment.install.ClasspathGenerator'

function RT {
    ( killProcess $@ --wait 10;
      nohup startProcess $@ </dev/null 1>$GENESIS_HOME/runtime/tmp/$@ 2>&1;

      sleep 3 ;
      tail -50 $GENESIS_HOME/runtime/tmp/$@
      rm $GENESIS_HOME/runtime/tmp/$@

      LOG_FILE=$L/$@.log
      LOG_FILE_PRESENT=false
      ATTEMPTS=0
      while [ $LOG_FILE_PRESENT = false ] && [ $ATTEMPTS -le 20 ]
      do
        if [ -f "$LOG_FILE" ]; then
            LOG_FILE_PRESENT=true
        else
            let "ATTEMPTS+=1"
            sleep 1
        fi
      done

      if [ $LOG_FILE_PRESENT = true ]; then
          tail -50f $L/$@.log
      else
          echo "Something has gone wrong, log file was not created after 20 seconds of process restart."
      fi )
}

# ----------------------------------------------------------------------------------------
# set auto-completion for start/killProcess process names and start/killGroup group names
# ----------------------------------------------------------------------------------------
_dta_process_list_options()
{
  procs=""
  if [ -f $GC/processes.xml ]; then
    procs=$(cat $GC/processes.xml | grep "<process name" | sed -e 's,.*<process\ name=\"\([^<]*\)\">.*,\1,g')
  fi

  local curr_arg;

  curr_arg=${COMP_WORDS[COMP_CWORD]}

  COMPREPLY=( $(compgen -W '$procs' -- $curr_arg ) );

}

complete -F _dta_process_list_options RT
complete -F _dta_process_list_options startProcess
complete -F _dta_process_list_options killProcess

_dta_process_group_list_options()
{
  groups=""
  if [ -f $GC/processes.xml ]; then
    groups=$(cat $GC/processes.xml | grep "<groupId" | sed -e 's,.*<groupId>\([^<]*\)</groupId>.*,\1,g' | uniq)
  fi

  local curr_arg;

  curr_arg=${COMP_WORDS[COMP_CWORD]}

  COMPREPLY=( $(compgen -W '$groups' -- $curr_arg ) );

}

complete -F _dta_process_group_list_options startGroup
complete -F _dta_process_group_list_options killGroup


