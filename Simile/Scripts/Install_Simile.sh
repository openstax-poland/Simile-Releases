#!/bin/bash
update_system_command="sudo apt update"
current_path="$(cd "$(dirname "$0")"; pwd -P)"
cd $current_path
echo "$current_path"
install_homebrew="sudo ./Scripts/installation-mac/Install_Homebrew.sh"
install_yarn_and_node="./Scripts/installation-mac/Install_YarnAndNode.sh"
install_python3="./Scripts/installation-mac/Install_Python3.sh"
install_opencv="sudo ./Scripts/installation-mac/Install_OpenCV.sh"
install_protocools="./Scripts/installation-mac/Install_Protocools.sh"
run_installation_verifier="./Scripts/Installation_Verifier.sh"

chmod_scanning_stopper="sudo chmod +x ./script.sh"
chmod_simile="sudo chmod +x ./run.sh"
chmod_homebrew="sudo chmod +x ./Scripts/installation-mac/Install_Homebrew.sh"
chmod_yarnandnode="sudo chmod +x ./Scripts/installation-mac/Install_YarnAndNode.sh"
chmod_python3="sudo chmod +x ./Scripts/installation-mac/Install_Python3.sh"
chmod_opencv="sudo chmod +x ./Scripts/installation-mac/Install_OpenCV.sh"
chmod_protocools="sudo chmod +x ./Scripts/installation-mac/Install_Protocools.sh"
chmod_installation_verifier="sudo chmod +x ./Scripts/Installation_Verifier.sh"

exec $update_system_command & wait

exec $chmod_homebrew & wait
exec $install_homebrew & wait

exec $chmod_yarnandnode & wait
exec $install_yarn_and_node & wait

exec $chmod_python3 & wait
exec $install_python3 & wait

exec $chmod_opencv & wait
exec $install_opencv & wait

exec $chmod_protocools & wait
exec $install_protocools & wait

exec $chmod_installation_verifier & wait
exec $run_installation_verifier & wait