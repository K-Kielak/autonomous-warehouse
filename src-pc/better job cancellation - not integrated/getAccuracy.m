function accuracy = getAccuracy(theta, X, y)

m = length(y);
output = threshold(sigmoid(X*theta'));
correctCount = 0;
for i = 1:m
	if(output(i) == y(i))
		correctCount++;
 	endif
endfor

accuracy = correctCount*100/m;

end
