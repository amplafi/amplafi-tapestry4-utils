package org.amplafi.tapestry4.asset;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.asset.ContextAssetFactory;

/**
 * A {@link ContextAssetFactory} that can version the url of the context resources.
 *  
 * @author andyhot
 * @see VersionedAsset
 */
public class AmplafiContextAssetFactory extends ContextAssetFactory {
	
	private String version;
	
	@Override
	public IAsset createAsset(Resource resource, Location location) {
		return new VersionedAsset(super.createAsset(resource, location), version);
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void initializeService() {
		if (version==null) {
			version = "" + System.currentTimeMillis()%100000;
		}
	}
}
