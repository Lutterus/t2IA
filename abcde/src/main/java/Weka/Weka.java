package Weka;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;

import Bow.BoW;
import Bow.GramaticalClass;
import Bow.gramaticalClassList;
import Storage.Storage;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class Weka {
	private FastVector attributesToken;
	private FastVector attributesClasses;
	private Instances treino;
	private Instances teste;
	private Instances temp;

	public Weka() {
	}

	public void createStruct(BoW bow) {
		attributesToken = new FastVector();
		attributesClasses = new FastVector();
		
		ArrayList<String> moreRelevantTerms = bow.getMoreRelevantTerms();
		ArrayList<gramaticalClassList> classes = bow.getPermanentClasses();

		// - numeric
		for (int i = 0; i < moreRelevantTerms.size(); i++) {
			attributesToken.addElement(new Attribute("P" + (i + 1)));
		}

		// - nominal
		for (gramaticalClassList gramaticalClassList : classes) {
			if (gramaticalClassList.getClassName().contentEquals("CLASSES") == false) {
				attributesClasses.addElement(gramaticalClassList.getClassName());
			}

		}
		attributesToken.addElement(new Attribute("classe", attributesClasses));

		// 2. create Instances object
		treino = new Instances("Treino", attributesToken, 0);
		teste = new Instances("Teste", attributesToken, 0);

		// 3. fill with data
		double[] dataVector;
		for (gramaticalClassList questionList : classes) {
			double divadePercentage = (0.8 * questionList.getGramaticalClassList().size());
			double countClasses = 1;
			for (GramaticalClass wholeQuestion : questionList.getGramaticalClassList()) {
				dataVector = new double[treino.numAttributes()];
				int index = 0;
				for (int i = 0; i < dataVector.length - 1; i++) {
					dataVector[i] = 0;
				}
				index = 0;
				for (String terms : moreRelevantTerms) {
					for (String questionPeace : wholeQuestion.getSplitQuestionNormalized()) {
						if (questionPeace.contentEquals(terms)) {
							dataVector[index] = 1;
						}
					}
					index++;
				}

				// - nominal
				dataVector[dataVector.length - 1] = attributesClasses.indexOf(questionList.getClassName());
				// -- add instances
				if (divadePercentage >= countClasses) {
					treino.add(new DenseInstance(1.0, dataVector));
				} else {
					teste.add(new DenseInstance(1.0, dataVector));
				}

				countClasses++;

			}
		}

	}

	public void createArffTestandTraining() throws IOException {
		// 4. trainning data
		Instances dataSet = treino;
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File("./data/treino.arff"));
		saver.writeBatch();

		// 4. test data
		dataSet = teste;
		saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File("./data/teste.arff"));
		saver.writeBatch();
	}

	public void createArffTemp() throws IOException {
		// 4. trainning data
		Instances dataSet = treino;
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File("./data/temp.arff"));
		saver.writeBatch();
	}

	public void createTempStruct(BoW bow, String question, String clas, Storage storage) {
		attributesToken = new FastVector();
		attributesClasses = new FastVector();
		
		ArrayList<String> moreRelevantTerms = bow.getMoreRelevantTerms();
		ArrayList<String> questionNormalized = bow.getQuestionNormalized(storage, question, clas);
		ArrayList<gramaticalClassList> classes = bow.getPermanentClasses();

		// - numeric
		for (int i = 0; i < moreRelevantTerms.size(); i++) {
			attributesToken.addElement(new Attribute("P" + (i + 1)));
		}

		// - nominal
		for (gramaticalClassList gramaticalClassList : classes) {
			if (gramaticalClassList.getClassName().contentEquals("CLASSES") == false) {
				attributesClasses.addElement(gramaticalClassList.getClassName());
			}

		}
		attributesToken.addElement(new Attribute("classe", attributesClasses));

		// 2. create Instances object
		treino = new Instances("Treino", attributesToken, 0);
		temp = new Instances("Teste", attributesToken, 0);

		// 3. fill with data
		double[] dataVector = null;

		for (gramaticalClassList questionList : classes) {
			for (GramaticalClass wholeQuestion : questionList.getGramaticalClassList()) {
				dataVector = new double[treino.numAttributes()];
				int index = 0;
				for (int i = 0; i < dataVector.length - 1; i++) {
					dataVector[i] = 0;
				}
				index = 0;
				for (String terms : moreRelevantTerms) {
					for (String questionPeace : wholeQuestion.getSplitQuestionNormalized()) {
						if (questionPeace.contentEquals(terms)) {
							dataVector[index] = 1;
						}
					}
					index++;
				}

				// - nominal
				dataVector[dataVector.length - 1] = attributesClasses.indexOf(questionList.getClassName());
				// -- add instances
				treino.add(new DenseInstance(1.0, dataVector));
			}
		}

		int index=0;
		for (String term : moreRelevantTerms) {
			for (String questionPeace : questionNormalized) {
				if (questionPeace.contentEquals(term)) {
					dataVector[index] = 1;
				}
			}
		}
		
		// - nominal
		boolean falsiable = false;
		for (gramaticalClassList gc : classes) {
			if(gc.getClassName().contentEquals(clas)) {
				dataVector[dataVector.length - 1] = attributesClasses.indexOf(gc.getClassName());
				falsiable = true;
			}
		}
		
		if(falsiable==true) {
			temp.add(new DenseInstance(1.0, dataVector));
		}else {
			System.err.println("não foi possivel encontrar esta classe nos nossos arquivos");
			throw new EmptyStackException();
		}
		
	}

	public void test(String string) throws Exception {
		if (string.contentEquals("treino")) {
			System.out.println("Iniciando teste do arquivo");
		} else if (string.contentEquals("temp")) {
			System.out.println("Iniciando o teste da palavra");
		}

		DataSource trainingDataSource = new DataSource("./data/treino.arff");
		DataSource testingDataSource = new DataSource("./data/" + string + ".arff");

		Instances training = trainingDataSource.getDataSet();
		training.setClassIndex(training.numAttributes() - 1);

		Instances testing = testingDataSource.getDataSet();
		testing.setClassIndex(testing.numAttributes() - 1);

		// Use a set of classifiers
		Classifier[] models = { new MultilayerPerceptron(), new RandomForest(), new BayesNet() };

		// Run for each model
		for (int j = 0; j < models.length; j++) {

			// Collect every group of predictions for current model in a FastVector
			FastVector predictions = new FastVector();

			// For each training-testing split pair, train and test the classifier
			Evaluation validation = classify(models[j], training, testing);

			predictions.appendElements(validation.predictions());

			// Uncomment to see the summary for each training-testing pair.
			// System.out.println(models[j].toString());

			// Calculate overall accuracy of current classifier on all splits
			double accuracy = calculateAccuracy(predictions);

			// Print current classifier's name and accuracy in a complicated,
			// but nice-looking way.
			System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
					+ String.format("%.2f%%", accuracy) + "\n---------------------------------");
		}
	}

	private double calculateAccuracy(FastVector predictions) {
		double correct = 0;
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}

		return 100 * correct / predictions.size();
	}

	private Evaluation classify(Classifier model, Instances training, Instances testing) {
		Evaluation evaluation = null;
		try {
			evaluation = new Evaluation(training);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			model.buildClassifier(testing);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			evaluation.evaluateModel(model, testing);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return evaluation;
	}
}
