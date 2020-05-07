#!/bin/bash
script_path="$(cd "$(dirname "$0")"; pwd -P)"
simile_run_filename="Simile-0.1-jar-with-dependencies.jar"
exec java -jar "${simile_run_filename}"


