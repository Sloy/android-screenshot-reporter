//This file has a different package to be able to access the protected method "baseLayer()" in "GraphHolder"
package android.support.test.espresso

/**
 * From Barista.
 *
 * Hacky method to access the current global FailureHandler used by Espresso.
 * This is the getter equivalent to Espresso.setFailureHandler()
 */
internal fun getCurrentFailureHandler(): FailureHandler = GraphHolder.baseLayer().failureHolder().get()