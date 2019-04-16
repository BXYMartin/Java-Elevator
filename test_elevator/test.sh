#!/bin/bash
num=`cat num.cache`
for ((i=1;i<=num;i++))
do
	# current=`date +%d%H%M%S`
	test_file="run.tst"
	result_file="run.res"
	error_file="run.err"
	rm run.*
	rm comp.*
	python comm.py
	cat $test_file >> comp.txt
	echo "END" >> comp.txt
	cat comp.res >> comp.txt
	echo "END" >> comp.txt
	cat $test_file >> run.txt
	echo "END" >> run.txt
	cat $result_file >> run.txt
	echo "END" >> run.txt
	java_start_test="java -jar elevator_tester.jar"
	success=$(cat run.txt | $java_start_test)
	if [ "$success" = 'Success!' ];
	then
		success=$(cat comp.txt | $java_start_test)
		if [ "$success" = 'Success!' ];
		then
			echo -e "[*] SUCCESS:\t $i/$num"
		else
			echo -e "[!] ERROR:\t Base Scheduler Failure!"
			break
		fi
	else
		echo -e "[!] ERROR:\t Fast Scheduler Failure!"
		break
	fi
done
