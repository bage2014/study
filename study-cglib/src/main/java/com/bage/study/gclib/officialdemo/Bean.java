package com.bage.study.gclib.officialdemo;

import java.beans.PropertyChangeListener;

/**
*
* @author  baliuka
*/
public abstract class Bean implements java.io.Serializable{
  
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
String sampleProperty;
   
 abstract public void addPropertyChangeListener(PropertyChangeListener listener); 
  
 abstract public void removePropertyChangeListener(PropertyChangeListener listener);
  
  public String getSampleProperty(){
     return sampleProperty;
  }
  
  public void setSampleProperty(String value){
     this.sampleProperty = value;
  }
  
  public String toString(){
    return "sampleProperty is " + sampleProperty;
  }
   
}
