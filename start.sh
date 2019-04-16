#!/bin/bash
if [ ! -d "test_elevator" ]; then
	echo "Dependency Directory test_elevator Not Found!"
	exit 1
fi
if [ $# -gt 0 ]; then
	echo "Setting Cached Parameters: Test_Rounds Max_Requests Max_Interval Java_Main_Path"
	
	echo "$1" > test_elevator/num.cache
	if [ $# -gt 1 ]; then
		echo "$2" > test_elevator/request.cache
	fi
	if [ $# -gt 2 ]; then
		echo "$3" > test_elevator/interval.cache
	fi
	if [ $# -gt 3 ]; then
		echo "$4" > test_elevator/project.cache
	fi
	uname > test_elevator/system.cache
else
	if [ ! -f "test_elevator/num.cache" ]; then
		echo "Parameters Test_Rounds Unset!"
		echo "Try Setting Parameters By:"
		echo -e "\t./start.sh Test_Rounds Max_Requests Max_Interval Java_Main_Path"
		exit 1
	fi
	if [ ! -f "test_elevator/request.cache" ]; then
		echo "Parameters Max_Requests Unset!"
		echo "Try Setting Parameters By:"
		echo -e "\t./start.sh Test_Rounds Max_Requests Max_Interval Java_Main_Path"
		exit 1
	fi
	if [ ! -f "test_elevator/interval.cache" ]; then
		echo "Parameters Max_Interval Unset!"
		echo "Try Setting Parameters By:"
		echo -e "\t./start.sh Test_Rounds Max_Requests Max_Interval Java_Main_Path"
		exit 1
	fi
	if [ ! -f "test_elevator/project.cache" ]; then
		echo "Parameters Java_Main_Path Unset!"
		echo "Try Setting Parameters By:"
		echo -e "\t./start.sh Test_Rounds Max_Requests Max_Interval Java_Main_Path"
		exit 1
	fi
	echo "Starting Elevator Autotest..."
	cd test_elevator
	num=`cat num.cache`
	request=`cat request.cache`
	interval=`cat interval.cache`
	project=`cat project.cache`
	echo -e "Current Parameters:\n\tRounds   :\t${num}\n\tRequests :\t${request}\n\tInterval :\t${interval}s\n\tMain     :\t\"${project}\""
	./test.sh
fi
