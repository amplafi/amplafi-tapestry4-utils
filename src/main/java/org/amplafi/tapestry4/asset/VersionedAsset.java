package org.amplafi.tapestry4.asset;

import java.io.InputStream;


import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IAsset;

/**
 * An {@link IAsset} decorator that adds a version number to the end of
 * url of resources (in order to defeat caching).
 *
 * @author andyhot
 * @see AmplafiClasspathAssetFactory
 * @see AmplafiContextAssetFactory
 */
public class VersionedAsset implements IAsset {

	private IAsset asset;
	private String version;

	public VersionedAsset(IAsset asset, String version) {
		this.asset = asset;
		this.version = version;
	}

	// TODO: This could also allow serving assets by totally different servers
	// TODO: Another way to version is to introduce a virtual folder (instead of the query string)
	@Override
	public String buildURL() {
		String url = asset.buildURL();
		// only version .css and .js
		if (url.endsWith(".js") || url.endsWith(".css")) {
			url += "?" + version;
		}
		return url;
	}

	@Override
	public InputStream getResourceAsStream() {
		return asset.getResourceAsStream();
	}

	@Override
	public Resource getResourceLocation() {
		return asset.getResourceLocation();
	}

	@Override
	public Location getLocation() {
		return asset.getLocation();
	}

	@Override
    public String toString() {
	    return "VersionedAsset("+this.version+") "+ this.asset;
	}
}
