package EffectiveMobile.testTask.security;

import ch.qos.logback.core.net.ObjectWriter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private static final String SECRET_KEY =
      "b2ec761bd8597c6f298695d29be8333698a3d9d7d647a8d574af7542f21ad788b2b8b1572c4abce8a74bafb6c85c89c754a435b6556b87c5c329f76e6f857bad599be4ec75aa80fd9729588509f55d5f1fc7533e6904f4f0a83afbbd08a9c1f3ad195ed183c4305ca5381057b9a9a0fc0d00add7cd81fae555b272f2597f637580e45d51025a9fb98c87ed161f2ede130d26d40d04d0f973b9717203b2382cc64c9ddde8be7bccb1869e255b3f7879c54e3d35277a00f1281a4fa5142abddc168726374c4598bdf31e739923a438cdf66016de4c884542708814c35e72bfa62b1ea2ebaa440bea04f9e63385e39467763dcaa03fe224a2e8b311e296745e84f6";

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails){
      return generateToken(new HashMap<>(), userDetails);
  }
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }


  public boolean isTokenValid(String token, UserDetails userDetails){
      final String username = extractUsername(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

    private boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
