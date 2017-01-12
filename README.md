# ImageRecognizer-Android
Image classification using neural networks (inception-bn) and MxNet (neural net library), implemented for Android.
#
[*nn/NNManager.java*](https://github.com/dneprDroid/ImageRecognizer/blob/master/app/src/main/java/neural/imagerecognizer/app/nn/NNManager.java) - class working with MxNet API

*res/raw/params* - serialized data of the network (weights, convolutional kernels)

*res/raw/symbol.json* - structure of the network 

*res/raw/syncet.txt* - word dictionary for network, pair output value - meaning word
 
#
<image src=https://raw.githubusercontent.com/dneprDroid/ImageRecognizer/master/images/Screenshot1.png height=500 />
#NDK library
Build **libmxnet_predict.so** from official mxnet sources - https://github.com/dmlc/mxnet/tree/master/amalgamation
#iOS
iOS version -  https://github.com/dneprDroid/ImageRecognizer-iOS

#Links
  * https://github.com/dmlc/mxnet - MxNet library 
  * https://culurciello.github.io/tech/2016/06/04/nets.html - architectures of neural nets, including inception-bn arch.
  * https://github.com/Trangle/mxnet-inception-v4 - inceprion network trainer

