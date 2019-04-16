#!/bin/bash
num=`cat num.cache`
for ((i=1;i<=num;i++))
do
	# current=`date +%d%H%M%S`
	test_file="run.tst"
	result_file="run.res"
	error_file="run.err"
    catch1=$(rm run.*)
    # catch2=$(rm comp.*)
	python comm.py
	cat $test_file >> run.txt
	echo "END" >> run.txt
	cat $result_file >> run.txt
	echo "END" >> run.txt
	java_start_test="java -jar elevator_tester.jar"
	success=$(cat run.txt | $java_start_test)
	if [ "$success" = 'Success!' ];
	then
        echo -e "[*] SUCCESS:\t $i/$num"
	else
        echo -e "$success"
		echo -e "[!] ERROR:\t Fast Scheduler Failure!"
		break
	fi
done
