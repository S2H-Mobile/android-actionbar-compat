package de.s2hmobile.compat.actionbar;

/**
 * This interface is implemented by all public base activities in the library.
 * It consists of all methods that define the behaviour of the action bar and
 * its items.
 * 
 * @author Stephan Hoehne
 * 
 */
public interface ActionBarConfigurator {

	/**
	 * Defines the behaviour of the home icon in the action bar.
	 * 
	 * @return True if the home icon is to be stateful.
	 */
	public boolean isHomeStateful();
}
