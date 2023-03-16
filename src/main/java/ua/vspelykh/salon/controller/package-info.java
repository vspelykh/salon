/**
 * The controller package contains classes for managing user requests and responses.
 * These classes implement the FrontController pattern to centralize request handling
 * and provide a single entry point for all requests.
 * <p>
 * The package includes the following classes:
 * <ul>
 *     <li> {@link ua.vspelykh.salon.controller.Controller}: a servlet that receives all user requests
 *         and routes them to the appropriate handler method.</li>
 *     <li>{@link ua.vspelykh.salon.controller.ControllerConstants}: a class that defines constants for use by the
 *         controller classes, such as attribute names and view paths.</li>
 * </ul>
 *
 * The "command" package contains classes that implement the Command pattern to encapsulate request handling
 * logic into separate classes.
 *
 * The "filter" package contains classes that implement the Filter pattern to intercept and manipulate
 * requests and responses before they reach the controller or after they leave it, such as to perform security checks,
 * change language of pages.
 */
package ua.vspelykh.salon.controller;