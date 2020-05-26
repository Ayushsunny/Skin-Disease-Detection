#import matplotlib
#import matplotlib.pyplot as plt
#import matplotlib.cm as cm
from urllib import urlretrieve
import cPickle as pickle
import os
import gzip
import numpy as np
import theano
import lasagne
from lasagne import layers
import sys
from PIL import Image
from lasagne.updates import nesterov_momentum
from nolearn.lasagne import NeuralNet
from nolearn.lasagne import visualize
from sklearn.metrics import classification_report
from sklearn.metrics import confusion_matrix
from sklearn.metrics import classification_report, accuracy_score

from nolearn.decaf import ConvNetFeatures
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
from sklearn.pipeline import Pipeline
from sklearn.utils import shuffle
from sklearn import svm
from sklearn.externals import joblib

TRAIN_DATA_DIR = '/home/pratik/Desktop/Skinzy Code SVM/Datasets2/train/'
TEST_DATA_DIR = '/home/pratik/Desktop/Skinzy Code SVM/Datasets2/test/'
#MODEL_FILE_KNN = '/home/varsha/Documents/Skinzy Code SVM/AndroidFileUpload/classifier_2disease/KNN.model'

"""def load_dataset():
    url = 'http://deeplearning.net/data/mnist/mnist.pkl.gz'
    filename = 'mnist.pkl.gz'
    if not os.path.exists(filename):
        print("Downloading MNIST dataset...")
        urlretrieve(url, filename)
    with gzip.open(filename, 'rb') as f:
        data = pickle.load(f)
    X_train, y_train = data[0]
    X_val, y_val = data[1]
    X_test, y_test = data[2]
    X_train = X_train.reshape((-1, 1, 28, 28))
    X_val = X_val.reshape((-1, 1, 28, 28))
    X_test = X_test.reshape((-1, 1, 28, 28))
    y_train = y_train.astype(np.uint8)
    y_val = y_val.astype(np.uint8)
    y_test = y_test.astype(np.uint8)
    return X_train, y_train, X_val, y_val, X_test, y_test"""

def load_train():
    scabies_dir = TRAIN_DATA_DIR + 'Scabies/'
    scabies_filenames = [scabies_dir + fn for fn in os.listdir(scabies_dir)]
    f_dir = TRAIN_DATA_DIR + 'Furruncle/'
    f_filenames = [f_dir + fn for fn in os.listdir(f_dir)]
    p_dir = TRAIN_DATA_DIR + 'Psoriasis/'
    p_filenames = [p_dir + fn for fn in os.listdir(p_dir)]
    labels = [0.0] * len(scabies_filenames) + [1.0] * len(p_filenames)

    
    #filenames = eczema_filenames + other_filenames
    #X,y= shuffle(filenames, labels, random_state=0)
    #X_train , y_train=X[:240] , y[:240]
    #X_val , y_val =X[240:] , y[240:]
    #X_train = X_train.reshape((-1, 3, 32, 32))/np.float32(256)
    #X_val = X_val.reshape((-1, 3, 32, 32))/np.float32(256)
    #y_train = y_train.astype(np.uint8)
    #y_val = y_val.astype(np.uint8)
    return labels

def load_test():
    scabies_dir = TEST_DATA_DIR + 'Scabies/'
    scabies_filenames = [scabies_dir + fn for fn in os.listdir(scabies_dir)]

    f_dir = TEST_DATA_DIR + 'Furruncle/'
    f_filenames = [f_dir + fn for fn in os.listdir(f_dir)]

    p_dir = TEST_DATA_DIR + 'Psoriasis/'
    p_filenames = [p_dir + fn for fn in os.listdir(p_dir)]

    labels = [0.0] * len(scabies_filenames) + [1.0] * len(p_filenames)
   
    #filenames = eczema_filenames + other_filenames
    #X_test,y_test= shuffle(filenames, labels, random_state=0)
    #X_test = (X_test.reshape((-1, 3, 32, 32)))/np.float32(256)
    #y_test = y_test.astype(np.uint8)
    return labels


def load_images(filename):
        if not os.path.exists(filename):
            download(filename)
        # Read the inputs in Yann LeCun's binary format.
        with gzip.open(filename, 'rb') as f:
            data = np.frombuffer(f.read(), np.uint8)
        # The inputs are vectors now, we reshape them to monochrome 2D images,
        # following the shape convention: (examples, channels, rows, columns)
        data = data.reshape(-1, 3, 64, 64)
        # The inputs come as bytes, we convert them to float32 in range [0,1].

        return data / np.float32(256)
    
def main():
    # my code here
    y_train=load_train()
    y_test =load_test()
    X_train=load_images("/home/pratik/Desktop/Skinzy Code SVM/Datasets2/scabies_train.gz")
    X_test=load_images("/home/pratik/Desktop/Skinzy Code SVM/Datasets2/scabies_test.gz")
    X_train,y_train= shuffle(X_train, y_train, random_state=0)
    X_val=X_train[80:]
    X_train=X_train[:80]
    y_val=y_train[80:]
    y_train=y_train[:80]
    y_train =np.array(y_train)
    y_test=np.array(y_test)
    y_val=np.array(y_val)
    y_test = y_test.astype(np.uint8)
    y_train = y_train.astype(np.uint8)
    y_val = y_val.astype(np.uint8)
    X_train,y_train= shuffle(X_train, y_train, random_state=0)
    #print("Plotting the graph")
    #plt.imshow(X_train[0][0])
   
    
    net1 = NeuralNet(
    layers=[('input', layers.InputLayer),
            ('conv2d1', layers.Conv2DLayer),
            ('maxpool1', layers.MaxPool2DLayer),
            ('conv2d2', layers.Conv2DLayer),
            ('maxpool2', layers.MaxPool2DLayer),
            ('dropout1', layers.DropoutLayer),
            ('dense', layers.DenseLayer),
            ('dropout2', layers.DropoutLayer),
            ('output', layers.DenseLayer),
            ],
   # input layer
    input_shape=(None, 3, 64,64),
    # layer conv2d1
    conv2d1_num_filters=32,
    conv2d1_filter_size=(5, 5),
    conv2d1_nonlinearity=lasagne.nonlinearities.rectify,
    conv2d1_W=lasagne.init.GlorotUniform(),  
    # layer maxpool1
    maxpool1_pool_size=(3, 3),
    maxpool1_stride=1,
    maxpool1_pad=0,    
    # layer conv2d2
    conv2d2_num_filters=32,
    conv2d2_filter_size=(5, 5),
    conv2d2_nonlinearity=lasagne.nonlinearities.rectify,
    # layer maxpool2
    maxpool2_pool_size=(3, 3),
    maxpool2_stride=1,
    maxpool2_pad=0,
    # dropout1
    dropout1_p=0.5,    
    # dense
    dense_num_units=256,
    dense_nonlinearity=lasagne.nonlinearities.rectify,    
    # dropout2
    dropout2_p=0.5,    
    # output
    output_nonlinearity=lasagne.nonlinearities.softmax,
    output_num_units=2,
    # optimization method params
    update=nesterov_momentum,
    update_learning_rate=0.001,
    update_momentum=0.9,
    max_epochs=50,
    verbose=1,
    )
    print ("Training starts :")
    net1.fit(X_train, y_train)

    #preds = net1.predict(X_test[0])
     	
    out = X_train[0].reshape(-1, 3, 64, 64)
    pred=net1.predict_proba(out)
    print pred
    #cm = confusion_matrix(y_test, preds)
    #plt.matshow(cm)
    #plt.title('Confusion matrix')
    #plt.colorbar()
    #plt.ylabel('True label')
    #plt.xlabel('Predicted label')
    #plt.show()
    
    #print (net1.predict_proba(X_test))
     
    sys.setrecursionlimit(9999999)
    joblib.dump(net1, 'AndroidFileUpload/classifier_2disease/cnn_1.pkl',compress=9)
    
    y_true, y_pred = y_test, net1.predict(X_test) # Get our predictions
    print(classification_report(y_true, y_pred)) # Classification on each digit
    print 'The accuracy is:', accuracy_score(y_true, y_pred)


   # visualize.plot_conv_weights(net1.layers_['conv2d1'])

    dense_layer = layers.get_output(net1.layers_['dense'], deterministic=True)
    output_layer = layers.get_output(net1.layers_['output'], deterministic=True)
    input_var = net1.layers_['input'].input_var
    f_output = theano.function([input_var], output_layer)
    f_dense = theano.function([input_var], dense_layer)

    instance = X_test[0][None, :, :]
    #%timeit -n 500 f_output(instance)
    train_features = f_dense(X_train)
    test_features=f_dense(X_test)
    train_labels=y_train
    test_labels=y_test

    '''
	Logistic Regression
	clf2 = LogisticRegression().fit(train_features, train_labels)
    joblib.dump(clf2, 'AndroidFileUpload/classifier_2disease/log_reg.pkl',compress=9)'''

    #SVM
    clf = svm.SVC(kernel="linear",C=10000.0,probability=True)
    clf.fit(train_features,train_labels)

    sys.setrecursionlimit(9999999)
    joblib.dump(clf, 'AndroidFileUpload/classifier_2disease/svm.pkl',compress=9)
    
    
    pred=clf.predict(test_features)
    print(classification_report(test_labels, pred))

    print 'The accuracy is:', accuracy_score(test_labels, pred)
    pred=clf.predict_proba(test_features)
    print pred

    '''#KNN
    from sklearn.neighbors import KNeighborsClassifier
    neigh = KNeighborsClassifier(n_neighbors=5)
    neigh.fit(train_features, train_labels)
    joblib.dump( neigh, MODEL_FILE_KNN,compress=9)

    pred=neigh.predict(test_features)
    print(classification_report(test_labels, pred))

    print 'The accuracy is:', accuracy_score(test_labels, pred)
    pred=neigh.predict_proba(test_features)
    print pred'''

    
    #N = pred.shape[1]
   # print (pred)
   # plt.bar(range(N), pred.ravel())
    #plt.show()


if __name__ == "__main__":
    main()
