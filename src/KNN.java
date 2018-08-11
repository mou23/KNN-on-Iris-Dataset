import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class KNN {
	int noOfAttributes;
	ArrayList<Flower>flowers;
	int k;

	public KNN (int k){
		flowers=new ArrayList<Flower>();
		noOfAttributes=4;
		this.k=k;
	}

	private void readFile()
	{
		try{
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("training.data")));
			String str;
			while((str=br.readLine())!=null){
				String []att=str.split(",");
				Flower f=new Flower(att[att.length-1]);
				for(int i=0;i<noOfAttributes;i++){
					f.attribute[i]=Double.valueOf(att[i]);
				}
				flowers.add(f);

			}
			br.close();
		}catch(Exception e){

			e.printStackTrace();
		}
	}
	
	public void runAlgorithm(){
		readFile();
		Flower f;
		int count=0;
		int total=0;
		try{
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("test.data")));
			String str;
			while((str=br.readLine())!=null){
				String []att=str.split(",");
				f=new Flower(att[att.length-1]);
				System.out.println("Expected class: "+f.className);
				for(int i=0;i<noOfAttributes;i++){
					f.attribute[i]=Double.valueOf(att[i]);
				}
				for(int j=0;j<flowers.size();j++){
					flowers.get(j).distance=calculateDistance(f.attribute,flowers.get(j).attribute);
				}
				Collections.sort(flowers, new Comparator<Flower>(){
					public int compare(Flower f1, Flower f2) {
						double dif=f1.distance-f2.distance;
						if(dif>0)
							return 1;
						else if(dif<0)
							return -1;
						else{
							return 0;
						}
					}
				});
				String className=findClass();
				if(className.equals(f.className)){
					count++;
				}
				System.out.println("Actual class: "+className);
				total++;
				System.out.println();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("\nAccuracy: "+ ((count*100)/total));
	}

	private double calculateDistance(double []d1,double[]d2)
	{
		double total=0;
		for(int i=0;i<noOfAttributes;i++){
			total+=(d1[i]-d2[i])*(d1[i]-d2[i]);
		}
		return (Math.sqrt(total));
	}

	private String findClass(){
		int setosa=0;
		int versicolor=0;
		int virginica=0;
		for(int i=0;i<k;i++){
			String className=flowers.get(i).className;
			if(className.equals("Iris-setosa")){
				setosa++;
			}
			else if(className.equals("Iris-versicolor")){
				versicolor++;
			}
			else{
				virginica++;
			}
		}
		
		if(setosa>=versicolor && setosa>=virginica){
			return "Iris-setosa";
		}
		else if(setosa<=versicolor && versicolor>=virginica){
			return "Iris-versicolor";
		}
		else{
			return "Iris-virginica";
		}
	}
}
