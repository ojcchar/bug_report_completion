par(mfrow=c(2,3))

a = c(84.47, 84.55, 81.01, 91.03, 88.76, 83.68, 84.35, 86.13, 89.13)
b=boxplot(a, ylim=c(30, 100))
points(mean(a),col="red")
b$stats[c(1, 5), ]

a = c(93.79, 94.9, 93.43, 91.03, 93.49, 92.44, 94.66, 87.41, 97.62)
b =boxplot(a, ylim=c(30, 100))
points(mean(a),col="red")
b$stats[c(1, 5), ]

a = c(88.89, 89.42, 86.78, 91.03, 91.07, 87.85, 89.21, 86.76, 93.18)
b =boxplot(a, ylim=c(30, 100))
points(mean(a),col="red")
b$stats[c(1, 5), ]

a = c(60.87, 68.49, 66.46, 82.46, 63.75, 75.11, 51.22, 73.26, 80.92)
b =boxplot(a, ylim=c(30, 100))
points(mean(a),col="red")
b$stats[c(1, 5), ]

a = c(68.29, 79.37, 96.46, 83.93, 86.44, 99.42, 36.84, 97.86, 98.15)
b =boxplot(a, ylim=c(30, 100))
points(mean(a),col="red")
b$stats[c(1, 5), ]

a = c(64.37, 73.53, 78.7, 83.19, 73.38, 85.57, 42.86, 83.79, 88.7)
b =boxplot(a, ylim=c(30, 100))
points(mean(a),col="red")
b$stats[c(1, 5), ]

#-------------------------------------

c = c(100.00, 88.89, 87.50, 80.00, 71.43, 65.31, 62.57, 58.49, 58.33, 58.14, 56.14, 53.97, 52.75, 50.00, 50.00, 34.51, 33.33, 33.33, 30.00, 28.57, 27.13, 25.00, 23.08, 17.76, 2.59, 0.00, 0.00, 0.00, 0.00, 0.00)
d = boxplot(c)
summary(c)


c =c(100.00, 66.67, 66.67, 63.64, 63.24, 60.52, 60.00, 56.25, 50.00, 50.00, 50.00, 48.28, 47.06, 47.06, 40.00, 38.71, 37.78, 33.33, 32.81, 25.00, 18.99, 12.96, 12.50, 4.76, 4.08, 0.91, 0.00, 0.00, 0.00, 0.00, 0.00)
summary(c)