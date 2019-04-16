from subprocess import Popen, PIPE
import os
import signal
import time
import re

talkpipe = Popen(['python', 'gen.py'],
    shell=False, stdout=PIPE)
with open("project.cache","r") as file:
    project = str(file.readline().strip('\n'))
with open("run.res","wb") as out, open("run.err","wb") as err, open('comp.res',"wb") as comp, open('comp.err',"wb") as comperr:
    elevator_fast = Popen(['java', '-classpath', project + ':elevator-input-hw2-1.3-jar-with-dependencies.jar:timable-output-1.0-raw-jar-with-dependencies.jar', 'Main'], stdin=PIPE, stdout=out, stderr=err, shell=False)
    elevator_std = Popen(['java', '-jar', 'oo-codes-6.jar'], stdin=PIPE, stdout=comp, stderr=comperr, shell=False)
start = time.time()
try:
    while True:
        line = talkpipe.stdout.readline()
        if line:
            elevator_fast.stdin.write(line)
            elevator_std.stdin.write(line)
            elevator_fast.stdin.flush()
            elevator_std.stdin.flush()
            with open("run.check","ab+") as check:
                check.write(str.encode("[{:.1f}]".format(time.time() - start))) 
                check.write(line)
        else:
            elevator_fast.stdin.close()
            elevator_std.stdin.close()
            break
        with open("run.tst","ab+") as test:
            test.write(line)
except KeyboardInterrupt:
    print("[!] ERROR:\t Terminating...")
    os.kill(talkpipe.pid, signal.SIGTERM)
try:
    ef = os.wait4(elevator_fast.pid, 200000)[2]
    elapsed = time.time() - start
    es = os.wait4(elevator_std.pid, 200000)[2]
    elapsed_all = time.time() - start
except ChildProcessError:
    print("[!] WARNING:\t Real Time Limit Exceeded!")
    os._exit(0)
except KeyboardInterrupt:
    print("[!] ERROR:\t Terminating...")
    os.kill(elevator_std.pid, signal.SIGTERM)
    os.kill(elevator_fast.pid, signal.SIGTERM)
if elevator_fast.poll() != 0:
    print("[!] ERROR:\t Error Status On Exit Fast Elevator!")
if elevator_std.poll() != 0:
    print("[!] ERROR:\t Error Status On Exit Standard Elevator!")
with open("run.res","r") as file:
    lines = file.readlines()
    time_fast = float(re.search(r"\d+\.?\d*", lines[-1]).group())
with open("comp.res","r") as file:
    lines = file.readlines()
    time_std = float(re.search(r"\d+\.?\d*", lines[-1]).group())

with open("system.cache","r") as system:
    line = str(system.readline().strip('\n'))
if line == "Darwin":
    data_check = Popen(['./datacheck_mac', '-i', 'run.check'], stdout=PIPE, shell=False)
elif line == "Linux":
    data_check = Popen(['./datacheck_ubuntu', '-i', 'run.check'], stdout=PIPE, shell=False)
else:
    print("[!] WARNING:\t Unsupported Platform Version!")
    data_check = Popen(['echo', 'max time is 0, base time is 0.'], stdout=PIPE, shell=False)
line, err = data_check.communicate()
try:
    time_max = float(re.search(r'max time is (\d+)', str(line)).group(1))
    time_bse = float(re.search(r'base time is (\d+)', str(line)).group(1))
except AttributeError:
    print("[!] ERROR:\t Case Too Long!")

print("[i] Baseline Refer:\t Base:{0:>3.3f} Upper :{0:>3.3f}".format(time_bse, time_max))
print("[i] Fast Scheduler:\t User:{0:>3.3f} System:{0:>3.3f}".format(elapsed, time_fast))
print("[i] Base Scheduler:\t User:{0:>3.3f} System:{0:>3.3f}".format(elapsed_all, time_std))
print("[-] Time Ratio:\t {0:>3.3f}".format((time_bse)/(time_fast)))
print("[-] Benchmark:\t {0:>3.3f}".format(time_std / time_fast))
if (time_fast / time_bse) > 1:
    with open("run.check","r") as test:
        print("[-] Bad Results:")
        for line in test.readlines():
            print(line.strip('\n'))
if (time_fast) > 1.05 * (time_std):
    print("[!] CPU MESSAGE:\t Fast: {0:>3.3f} Standard: {0:>3.3f}".format(ef.ru_utime, es.ru_utime))
    print("[!] FATAL ERROR:\t Time Limit Exceeded!")
    os._exit(0)
