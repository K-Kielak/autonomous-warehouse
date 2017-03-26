function omega = train(theta, XTrain, yTrain, alpha, XTest, yTest)


accuracy = 0;

while(accracy < 90)
	[costTrain, grad] = costFunction(theta, XTrain, yTrain);
	theta -= alpha.*grad;
	accuracy = getAccuracy(theta, XTest, yTest);
endwhile

end

