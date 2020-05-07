#!/bin/bash
## Script was created to easily check the installation status of necessary Simile's dependencies.

verify_java_installation() {
    JAVA_VERSION="$(command -v java)"
    if [[ $JAVA_VERSION == *"/java" ]]; then
        echo "Java -> OK"
    else
        echo "Java -> NOT INSTALLED!"
    fi
}

verify_node_installation() {
    NODE_VERSION="$(command -v node)"
    if [[ $NODE_VERSION == *"/node" ]]; then
        echo "NodeJS -> OK"
    else
        echo "NodeJS -> NOT INSTALLED!"
    fi
}

verify_yarn_installation() {
    YARN_VERSION="$(command -v yarn)"
    if [[ $YARN_VERSION == *"/yarn" ]]; then
        echo "Yarn -> OK"
    else
     echo "Yarn -> NOT INSTALLED!"
    fi
}

verify_python_installation() {
    PYTHON_VERSION="$(command -v python3)"
    if [[ $PYTHON_VERSION == *"/python3" ]]; then
        echo "Python -> OK"
    else
        echo "Python -> NOT INSTALLED!"
    fi
}

echo 'Verifying installation...'
verify_java_installation
verify_node_installation
verify_yarn_installation
verify_python_installation