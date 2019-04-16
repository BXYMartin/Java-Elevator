from subprocess import Popen, PIPE
import os
import signal
import time
import re

talkpipe = Popen(['python', 'gen.py'],
    shell=False, stdout=PIPE)
with open("project.cache","r") as file:
    project = str(file.readline().strip('\n'))
with open("package.cache","r") as file:
    package = str(file.readline().strip('\n'))
with open("run.res","wb") as out, open("run.err","wb") as err: # , open('comp.res',"wb") as comp, open('comp.err',"wb") as comperr:
    elevator_fast = Popen(['java', '-classpath', project + ':elevator-input-hw3-1.4-jar-with-dependencies.jar:timable-output-1.0-raw-jar-with-dependencies.jar', package], stdin=PIPE, stdout=out, stderr=err, shell=False)
    # elevator_std = Popen(['java', '-jar', 'oo-codes-6.jar'], stdin=PIPE, stdout=comp, stderr=comperr, shell=False)
start = time.time()
try:
    while True:
        line = talkpipe.stdout.readline()
        if line:
            elevator_fast.stdin.write(line)
            # elevator_std.stdin.write(line)
            elevator_fast.stdin.flush()
            # elevator_std.stdin.flush()
            with open("run.check","ab+") as check:
                check.write(str.encode("[{:.1f}]".format(time.time() - start)))
                check.write(line)
        else:
            elevator_fast.stdin.close()
            # elevator_std.stdin.close()
            break
        with open("run.tst","ab+") as test:
            test.write(line)
except KeyboardInterrupt:
    print("[!] ERROR:\t Terminating...")
    os.kill(talkpipe.pid, signal.SIGTERM)
try:
    timeout = 200
    t_beginning = time.time()
    seconds_passed = 0
    while True:
        ef = os.wait4(elevator_fast.pid, os.WNOHANG)[2]
        if elevator_fast.poll() is not None:
            break
        seconds_passed = time.time() - t_beginning
        if timeout and seconds_passed > timeout:
            elevator_fast.terminate()
            print("[!] ERROR:\t Elevator Running Timeout!")
            raise TimeoutError("Elapsed For " + str(timeout) + " Seconds")
        time.sleep(0.1)
    elapsed = time.time() - start
    # es = os.wait4(elevator_std.pid, 200000)[2]
    # elapsed_all = time.time() - start
except ChildProcessError:
    print("[!] WARNING:\t Real Time Limit Exceeded!")
    os._exit(0)
except KeyboardInterrupt:
    print("[!] ERROR:\t Terminating...")
    # os.kill(elevator_std.pid, signal.SIGTERM)
    os.kill(elevator_fast.pid, signal.SIGTERM)
if elevator_fast.poll() != 0:
    print("[!] ERROR:\t Error Status On Exit Fast Elevator!")
# if elevator_std.poll() != 0:
#     print("[!] ERROR:\t Error Status On Exit Standard Elevator!")
with open("run.res","r") as file:
    lines = file.readlines()
    time_fast = float(re.search(r"\d+\.?\d*", lines[-1]).group())
# with open("comp.res","r") as file:
#     lines = file.readlines()
#     time_std = float(re.search(r"\d+\.?\d*", lines[-1]).group())

time_max = 200
time_bse = 10

print("[i] Baseline Refer:\t Base :{0:>7.3f} | Upper:{1:>7.3f}".format(time_bse, time_max))
print("[i] Fast Scheduler:\t Total:{0:>7.3f} | CPU  :{1:>7.3f} | Kernel:{2:>7.3f}".format(elapsed, ef.ru_utime, ef.ru_stime))
# print("[i] Base Scheduler:\t User:{0:>3.3f} System:{0:>3.3f}".format(elapsed_all, time_std))
print("[-] Time Ratio:\t {0:>7.3f}".format((time_max)/(time_fast)))
# print("[-] Benchmark:\t {0:>3.3f}".format(time_std / time_fast))
with open("summary.log","a+") as log:
    log.write(str(time_fast) + "\n")
if (time_fast / time_max) > 1 or ef.ru_utime+ef.ru_stime > time_bse:
    with open("run.check","r") as test:
        print("[-] Bad Results:")
        for line in test.readlines():
            print(line.strip('\n'))
# if (time_fast) > 1.05 * (time_std):
#     print("[!] CPU MESSAGE:\t Fast: {0:>3.3f} Standard: {0:>3.3f}".format(ef.ru_utime, es.ru_utime))
#     print("[!] FATAL ERROR:\t Time Limit Exceeded!")
#     os._exit(0)
