/**
 * The filter package contains classes that implement filters to intercept and modify incoming and outgoing
 * requests and responses.
 * <p>
 * The package includes the following classes:
 * <ul>
 *     <li> EncodingFilter: a filter that sets the character encoding of incoming
 *         requests to UTF-8 to ensure proper handling of non-ASCII characters. </li>
 *     <li> LocalizationFilter: a filter that sets the locale of incoming requests
 *         based on the user's language preference or browser settings, to enable internationalization of
 *         the application. </li>
 *     <li> SecurityFilter: a filter that handles requests to secure resources and
 *         enforce access control policies, such as requiring authentication or authorization to access
 *         certain pages. </li>
 * </ul>
 */
package ua.vspelykh.salon.controller.filter;