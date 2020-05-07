#!/bin/bash
simile_run_filename="Simile-0.1-jar-with-dependencies.jar"
current_path="$(cd "$(dirname "$0")"; pwd -P)"
cd $current_path
exec java -jar "${simile_run_filename}"
