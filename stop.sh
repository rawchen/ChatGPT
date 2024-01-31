#!/bin/sh
PID=$(cat /var/run/ChatGPT.pid)
kill -9 $PID
