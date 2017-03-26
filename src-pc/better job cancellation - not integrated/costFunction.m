function[cost, grad] = costFunction(theta, X, y) 

m = length(y); %number of training examples

h = sigmoid(X*theta');
cost = (-y'*log(h) - (1-y)'*log(1-h))/m;
grad = (h-y)'*X/m;
end;


