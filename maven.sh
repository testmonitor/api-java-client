#!/bin/zsh

GPG_TTY=$(tty)
export GPG_TTY
mvn clean install -U
mvn assembly:single
mvn clean deploy
