/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.method;

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author shakil
 */

class Simplex {
    private final int rows;
    private final int columns;
    private final double[][] A;
    private final int problemType;
    private boolean unboundedSolution=false;
    public Simplex(int numberOfConstraints, int numberOfVariables, int problemType){
        this.rows=numberOfConstraints+1;
        this.columns=numberOfVariables+1;
        this.A=new double[this.rows][];
        for(int i=0;i<this.rows;i++){
            A[i]=new double[this.columns];
        }
        this.problemType=problemType;
    }

    public void table(double[][] data){
        for(int i=0;i<this.A.length;i++){
            System.arraycopy(data[i], 0, this.A[i], 0, data[i].length);
        }
    }
    public double[][] getA() {
        return this.A;
    }
    
    private void nextTable(int pivotRow, int pivotColumn){
        double pivotValue=this.A[pivotRow][pivotColumn];
        double[] pivotRowValues=new double[this.columns];
        double[] pivotColumnValues=new double[this.columns];
        double[] newRow=new double[this.columns];
        System.arraycopy(this.A[pivotRow], 0, pivotRowValues, 0, this.columns);
        for(int i=0;i<rows;i++){
            pivotColumnValues[i]=this.A[i][pivotColumn];
        }
        
        for(int i=0;i<this.columns;i++){
            newRow[i]= pivotRowValues[i]/pivotValue;
        }
        
        for(int i=0;i<this.rows;i++){
            if(i!=pivotRow){
                for(int j=0;j<this.columns;j++){
                    double temp=pivotColumnValues[i];
                    this.A[i][j]=this.A[i][j]-(temp*newRow[j]);
                }
            }
        }
        System.arraycopy(newRow, 0, this.A[pivotRow], 0, newRow.length);
    }
    
    private double[] ratio(int column){
        double[] positiveValues=new double[this.rows];
        double[] result=new double[this.rows];
        int negativeCount=0;
        for(int i=0;i<this.rows;i++){
            if(this.A[i][column]>0){
                positiveValues[i]=this.A[i][column];
            }else{
                positiveValues[i]=0;
                negativeCount++;
            }
        }
        if(negativeCount==this.rows){
            this.unboundedSolution=true;
        }else{
            for(int i=0;i<this.rows;i++){
                double temp=positiveValues[i];
                if(temp>0){
                    result[i]=this.A[i][this.columns-1]/temp;
                }
            }
        }     
        return result;
    }
    
    private int findEnteringColumn(){
        double[] values=new double[this.columns];
        int index=0;
        int pos;
        if(this.problemType==1){
            for(pos=0;pos<this.columns-1;pos++){
            if(this.A[this.rows-1][pos]<0){
                values[pos]=Math.abs(this.A[this.rows-1][pos]);
            }
        }
            index=max(values);
        }else{
            for(pos=0;pos<this.columns-1;pos++){
            if(this.A[this.rows-1][pos]>0){
                values[pos]=Math.abs(this.A[this.rows-1][pos]);
            }
        }
            index=max(values);
        }
        return index;
    }
    
    private int min(double[] array){
        double minValue;
        int i;
        int index=0;
        minValue=array[0];
        for(i=1;i<array.length;i++){
            if(array[i]>0){
                if(Double.compare(array[i], minValue)<0){
                    minValue=array[i];
                    index=i;
                }
            }
        }
        return index;
    }
    private int max(double[] array){
        double maxValue;
        int i;
        int index=0;
        maxValue=array[0];
        for(i= 1;i<array.length;i++){
            if(Double.compare(array[i], maxValue)>0){
                maxValue=array[i];
                index=i;
            }
        }
        return index;
    }
    
    public boolean checkOptimality(){
        boolean isOptimal=false;
        int count=0;
        if(this.problemType==1){
            for(int i=0;i<this.columns-1;i++){
                double temp=this.A[this.rows-1][i];
                if(temp>=0){
                    count++;
                }
            }
            if(count==this.columns-1){
                isOptimal=true;
            }
        }else{
            for(int i=0;i<this.columns-1;i++){
                double temp=this.A[this.rows-1][i];
                if(temp<=0){
                    count++;
                }
            }
            if(count==this.columns-1){
                isOptimal=true;
            }
        }
        return isOptimal;
    }
    
    public static enum ERROR{NotOptimal, IsOptimal, Unbounded};
    public ERROR solve(){
        if(checkOptimality()){
            return ERROR.IsOptimal;
        }
        int pivotColumn=findEnteringColumn();
        System.out.println("Pivot Column: "+pivotColumn);
        double[] ratios=ratio(pivotColumn);
        if(this.unboundedSolution==true){
            return ERROR.Unbounded;
        }
        int pivotRow=min(ratios);
        System.out.println("Pivot Row: "+pivotRow);
        nextTable(pivotRow, pivotColumn);
        return ERROR.NotOptimal;
    }

    public void print(){
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.columns;j++){
                String value=String.format("%.2f", this.A[i][j]);
                System.out.print(value+"\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}
public class SimplexMethod {

    /**
     * @param args the command line arguments
     */
    public static void introduction(){
        System.out.println("Note that:");
        System.out.println("1.This implementation of Simplex Method is able to solve problems in the form below:");
        System.out.println("Maximize/Minimize cx");
        System.out.println("Subject to Ax=<b and x>=0");
        System.out.println("2.c is 1*n");
        System.out.println("3.x is n*1");
        System.out.println("4.A is m*n");
        System.out.println("5.b is 1*n");
        System.out.println();
    }
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner input=new Scanner(System.in);
        introduction();
        System.out.println("What's your problem type?");
        System.out.println("1.Max");
        System.out.println("2.Min");
        int problemType=input.nextInt();
        System.out.println("Enter m and n respectivly:");
        int m=input.nextInt();
        int n=input.nextInt();
        System.out.println("Matrix A:");
        double[][] A=new double[m][n];
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                A[i][j]=input.nextDouble();
            }
        }
        
        boolean proceed=false;
        Simplex p=new Simplex(m-1, n-1, problemType);
        p.table(A);
        System.out.println("Starting");
        p.print();
        while(!proceed){
            Simplex.ERROR e=p.solve();
            p.print();
            if(e==Simplex.ERROR.IsOptimal){
                proceed=true;
            }else{
                if(e==Simplex.ERROR.Unbounded){
                System.out.println("Solution is unbounded");
                proceed=true;
            }
        }
    }
        //Examples
        //First example
        //Max 3x1+5x2
        //Subjet to x1+x2=<4
        //          x1+3x2=<6
        //x1, x2>=0
        //Output
        /*Starting
1.00	1.00	1.00	0.00	4.00	
1.00	3.00	0.00	1.00	6.00	
-3.00	-5.00	0.00	0.00	0.00	

Pivot Column: 1
Pivot Row: 1
0.67	0.00	1.00	-0.33	2.00	
0.33	1.00	0.00	0.33	2.00	
-1.33	0.00	0.00	1.67	10.00	

Pivot Column: 0
Pivot Row: 0
1.00	0.00	1.50	-0.50	3.00	
0.00	1.00	-0.50	0.50	1.00	
0.00	0.00	2.00	1.00	14.00	

1.00	0.00	1.50	-0.50	3.00	
0.00	1.00	-0.50	0.50	1.00	
0.00	0.00	2.00	1.00	14.00*/
        //The optimum value is 14
        
        //Second example
        //Min -x1+2x2
        //Subject to 3x1+4X2=<5
        //           2X1-3X2=<7
        //x1,x2>=0
        //Output
        /*Starting
3.00	4.00	1.00	0.00	5.00	
2.00	-3.00	0.00	1.00	7.00	
1.00	-2.00	0.00	0.00	0.00	

Pivot Column: 0
Pivot Row: 0
1.00	1.33	0.33	0.00	1.67	
0.00	-5.67	-0.67	1.00	3.67	
0.00	-3.33	-0.33	0.00	-1.67	

1.00	1.33	0.33	0.00	1.67	
0.00	-5.67	-0.67	1.00	3.67	
0.00	-3.33	-0.33	0.00	-1.67*/
        //The optimum value is -1.67
    }
}
