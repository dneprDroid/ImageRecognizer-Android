# ImageRecognizer-Android
Image classification using neural networks (inception-bn) and MxNet (neuralnet library), implemented for Android.
#
*res/raw/params* - serialized data of the network (weights, convolutional kernels)

*res/raw/symbol.json* - structure of the network 

*res/raw/syncet.txt* - word dictionary for network, pair output value - word meaning
#NDK library
Build **libmxnet_predict.so** from official mxnet sources - https://github.com/dmlc/mxnet
#iOS
iOS version -  https://github.com/dneprDroid/ImageRecognizer-iOS

#Links
  * https://github.com/dmlc/mxnet - MxNet library 
  * https://culurciello.github.io/tech/2016/06/04/nets.html - architectures of neural nets, including inception-bn arch.

