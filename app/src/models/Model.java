package models;

import java.io.Serializable;
import java.util.Observable;

/*
 * An abstract class that represents the model in an MVC design pattern.
 */
public abstract class Model extends Observable implements Serializable
{
	private String errorMessage = null;
	private boolean modelState = true;
	
	public boolean isModelValid()
	{
		return modelState;
	}
	
	public void makeModelValid()
	{
		errorMessage = null;
		modelState = true;
	}
	
	public void makeModelInvalid()
	{
		modelState = false;
	}
	
	public void addModelError(String errorMessage)
	{
		this.modelState = false;
		this.errorMessage = errorMessage;
	}
	
	public String getModelError()
	{
		return errorMessage;
	}
}
