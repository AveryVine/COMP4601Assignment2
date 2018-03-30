//package edu.carleton.comp4601.resources;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Random;
//
//public class Clustering {
//	ArrayList<Double> results;
//	ArrayList<User> users, clusterCentres;
//	String fileName;
//	int no_users, no_features, no_clusters;
//	Random random = new Random();
//	boolean changed;
//
//	public Clustering(ArrayList<User> users) {
//		this.users = users;
//	}
//
//	public void run() {
//		for (int i = 0; i < no_users; i++)
//			System.out.println(users.get(i).toString());
//
//		randomClusterCentreInitialization();
//		
//		changed = true;
//		while (changed) {
//			changed = false;
//			for (int i = 0; i < no_users; i++) {
//				for (int j = 0; j < no_clusters; j++) {
//					if (j != users.get(i).cluster) {
//						if (users.get(i).getCluster() == null
//								|| distance(users.get(i), clusterCentres.get(j)) < distance(users.get(i), users.get(i).getCluster())) {
//							users.get(i).cluster = j;
//							changed = true;
//						}
//					}
//				}
//			}
//
//			for (int i = 0; i < no_clusters; i++) {
//				clusterCentres.get(i).resetPosition();
//				int no_members = 0;
//				for (int j = 0; j < no_users; j++) {
//					if (i == users.get(j).cluster) {
//						no_members++;
//						for (int k = 0; k < no_features; k++) {
//							clusterCentres.get(i).features[k] += users.get(j).features[k];
//						}
//					}
//				}
//				if (no_members != 0) {
//					for (int j = 0; j < no_features; j++) {
//						clusterCentres.get(i).features[j] /= no_members;
//					}
//				}
//			}
//		}
//		for (int i = 0; i < no_clusters; i++) {
//			System.out.println(clusterCentres.get(i).toString());
//		}
//	}
//	
//	private static double distance(User a, User b) {
//		double rtn = 0.0;
//		// Assumes a and b have same number of features
//		for (int i = 0; i < a.features.length; i++) {
//			rtn += (a.features[i] - b.features[i]) * (a.features[i] - b.features[i]);
//		}
//		return Math.sqrt(rtn);
//	}
//	
//	private void randomClusterCentreInitialization() {
//		double featureMaxes[] = new double[no_features];
//		double featureMins[] = new double[no_features];
//		for (int i = 0; i < no_features; i++) {
//			featureMaxes[i] = users.get(i).features[0];
//			featureMins[i] = users.get(i).features[0];
//
//			for (int j = 1; j < no_users; j++) {
//				if (users.get(j).features[i] > featureMaxes[i]) {
//					featureMaxes[i] = users.get(j).features[i];
//				}
//				if (users.get(j).features[i] < featureMins[i]) {
//					featureMins[i] = users.get(j).features[i];
//				}
//			}
//		}
//
//		for (int i = 0; i < no_clusters; i++) {
//			clusterCentres.set(i, new User("clusterCentre" + i, no_features, no_clusters));
//			clusterCentres.get(i).setRandomPosition(featureMaxes, featureMins);
//		}
//	}
//
//}
