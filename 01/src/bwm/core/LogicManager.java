package bwm.core;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class LogicManager {

	boolean debug = true;
	float w0 = 1;
	float w1 = 1;
	float w2 = -1;

	public void resetConst(){
		w1 = 1;
		w2 = -1;
	}
	
//	public int checkSinglePointConstChange(Point p){
//		return checkSinglePointConstChange(p.getX1(),p.getX2(),p.getD());
//	}
	
	public int checkSinglePointConstChange(float x1, float x2, float d){
		float x0=1; //biast
		x1 = x1+5;
		x2 = x1+5;
	    //float y = Math.signum((w1*x1)+(w2*x2));
		float y = Math.signum((w0*x0)+(w1*x1)+(w2*x2));
	    
	    
	    //set new const values
	    /*
	    w1 = (float) (w1 + 0.02*(d-y)*x1);
	    w2 = (float) (w2 + 0.02*(d-y)*x2);
	    */
	    float diff = d-y;
	    w0 = (float) (w0 + 0.02*(diff)*x0);
	    w1 = (float) (w1 + 0.02*(diff)*x1); //
	    w2 = (float) (w2 + 0.02*(diff)*x2);
	    
	    //w1int = (int) (w1int + 0.02*(d-y)*x1);
	    //w2int = (int) (w2int + 0.02*(d-y)*x2);
	    if(debug){
	    boolean changed = false;
	    if(diff!=0) changed=true;
	    System.out.println("d["+d+"]"+"-y["+y+"] = "+(d-y)+" ; w0:"+w0+" ; w1:"+w1+" ; w2:"+w2+" ; changed: " + changed);
	    
	    }
	    //System.out.println("w1int:"+w1int+" ; w2int:"+w2int);
	    int result = Math.round(Math.signum((w1*x1)+(w2*x2)));
	        
	    return result;
		
	}
}
