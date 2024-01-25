package ua.vspelykh.gatewaymicroservice.filter;

//
////@Component
//@AllArgsConstructor
//public class JwtFilter implements WebFilter {
//
//    private final JwtProvider jwtProvider;
//    private final UserDetailsService userDetailsService;
//
//    @Override
//    @NonNull
//    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//
//        if (shouldNotFilter(request)) {
//            return chain.filter(exchange);
//        }
//
//        Map<String, String> headers = request.getHeaders().toSingleValueMap();
//        String token = jwtProvider.resolveToken(headers);
//
//        if (StringUtils.isEmpty(token)) {
//            throw new JwtAuthenticationException("No token provided");
//        }
//
//        if (jwtProvider.validateToken(token)) {
//            String id = jwtProvider.getUserIdFromToken(token);
//            try {
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(id);
//                Authentication auth = jwtProvider.getAuthentication(userDetails);
//
//                if (auth != null) {
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                }
//            } catch (Exception e) {
//                throw new FeignLoginException("Invalid request");
//            }
//        } else {
//            throw new InvalidJwtException("JWT token is expired or invalid");
//        }
//        return chain.filter(exchange);
//    }
//
//    private boolean shouldNotFilter(ServerHttpRequest request) {
//        String requestUri = request.getURI().getPath();
//        boolean isAllowedPath = Arrays.stream(SecurityConstants.NO_AUTH_PARTS)
//                .anyMatch(url -> requestUri.toLowerCase().contains(url));
//        return Arrays.stream(SecurityConstants.NO_AUTH_URLS)
//                .anyMatch(uri -> uri.equalsIgnoreCase(requestUri)
//                        || requestUri.matches(uri.replace("**", ".*"))) || isAllowedPath;
//    }
//}
//
