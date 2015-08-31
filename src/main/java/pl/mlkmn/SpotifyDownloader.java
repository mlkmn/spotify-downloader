package pl.mlkmn;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class SpotifyDownloader implements InitializingBean {

	private String authenticationCode = "";
	private SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture;
	
	@Autowired
	private SpotifyService spotifyService;
	@Autowired
	private SpotifyApiFactory spotifyApiFactory;
	@Autowired
	private SpotifyDownloaderService spotifyDownloaderService;

	private Api api;

	@RequestMapping(value = "/")
	public String index(ModelMap model) {
		if (isAuthenticateCodeSet()) {
			return "redirect:/dashboard?code=" + getAuthenticationCode();
		}
		model.put("authorizeUrl", spotifyService.getCreatedAuthorizeUrl(api));
		return "index";
	}
	
	@RequestMapping(value = "/dashboard")
	public String dashboard(@RequestParam("code") String code, ModelMap modelMap) throws Exception {
		if (!StringUtils.isEmpty(code)) {
			setAuthenticationCode(code);
		}

		if (authorizationCodeCredentialsFutureIsNotSet()) {
			setAuthorizationCodeCredentialsFuture(api.authorizationCodeGrant(getAuthenticationCode()).build()
					.getAsync());

			Futures.addCallback(authorizationCodeCredentialsFuture, new FutureCallback<AuthorizationCodeCredentials>() {
				@Override
				public void onSuccess(AuthorizationCodeCredentials authorizationCodeCredentials) {
					api.setAccessToken(authorizationCodeCredentials.getAccessToken());
					api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
				}

				@Override
				public void onFailure(Throwable throwable) {
					System.out.println(throwable.getMessage());
				}
			});
		}
		

		modelMap.put("userSimplePlaylists", spotifyService.getUserSimplePlaylists(api));

		return "dashboard";
	}

	@RequestMapping(value = "/playlist")
	public String playlist(@RequestParam("playlistId") String playlistId, @RequestParam("ownerId") String 
			ownerId, ModelMap modelMap) throws
			IOException, 
			WebApiException {
		List<SpotifyDownloaderResult> betterResults = spotifyDownloaderService.getResultsForPlaylist(api, ownerId,
				playlistId);
		String playlistName = spotifyService.getPlaylistName(api, ownerId, playlistId);

		modelMap.put("results", betterResults);
		modelMap.put("playlistName", playlistName);
		return "playlist";
	}

	@RequestMapping(value = "/logout")
	public String logout() {
		setAuthenticationCode("");
		setAuthorizationCodeCredentialsFuture(null);
		return "redirect:/";
	}
	
	private boolean isAuthenticateCodeSet() {
		return StringUtils.isNotEmpty(getAuthenticationCode());
	}
	
	private boolean authorizationCodeCredentialsFutureIsNotSet() {
		return getAuthorizationCodeCredentialsFuture() == null;
	}

	public SettableFuture<AuthorizationCodeCredentials> getAuthorizationCodeCredentialsFuture() {
		return authorizationCodeCredentialsFuture;
	}

	public void setAuthorizationCodeCredentialsFuture(SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture) {
		this.authorizationCodeCredentialsFuture = authorizationCodeCredentialsFuture;
	}

	public String getAuthenticationCode() {
		return authenticationCode;
	}

	public void setAuthenticationCode(String authenticationCode) {
		this.authenticationCode = authenticationCode;
	}

	public void setSpotifyApiFactory(SpotifyApiFactory spotifyApiFactory) {
		this.spotifyApiFactory = spotifyApiFactory;
	}

	public void setSpotifyService(SpotifyService spotifyService) {
		this.spotifyService = spotifyService;
	}

	public void setSpotifyDownloaderService(SpotifyDownloaderService spotifyDownloaderService) {
		this.spotifyDownloaderService = spotifyDownloaderService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		api = spotifyApiFactory.getApi();
	}
}