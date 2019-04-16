import random
import time, sys
def input_generator(testNum):
	tests = []
	realNum = random.randint(int(testNum*0.8), testNum)
	for i in range(realNum):
		fromFloor = random.randint(-3, 20)
		while fromFloor == 0:
			fromFloor = random.randint(-3, 20)
		toFloor = random.randint(-3, 20)
		while toFloor == 0 or fromFloor == toFloor:
			toFloor = random.randint(-3, 20)
		if fromFloor != toFloor:
			inputString = str(i+1) + '-FROM-' + str(fromFloor) + '-TO-' + str(toFloor);
			tests.append(inputString)
	return tests

with open("interval.cache","r") as file:
	interval = int(file.readline().strip('\n'))

with open("request.cache","r") as file:
	total = int(file.readline().strip('\n'))

tests = input_generator(total)
for each in tests:
	time.sleep(random.randint(0, 1000 * interval)/1000)
	print(each)
	sys.stdout.flush()
