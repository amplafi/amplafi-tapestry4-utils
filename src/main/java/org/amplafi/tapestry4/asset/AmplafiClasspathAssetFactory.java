package org.amplafi.tapestry4.asset;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.asset.ClasspathAssetFactory;

/**
 * A {@link ClasspathAssetFactory} that can version the url of the classpath resources.
 *
 * @author andyhot
 * @see VersionedAsset
 */
public class AmplafiClasspathAssetFactory extends ClasspathAssetFactory {
	private String version;

	@Override
	public IAsset createAsset(Resource resource, Location location) {
		return new VersionedAsset(super.createAsset(resource, location), version);
	}

	public void setVersion(String version) {
	    Defense.notNull(version, "version cannot be null");
		this.version = version;
	}
	public String getVersion() {
	    return this.version;
	}

	public void initializeService() {
	    if ( this.version == null || this.version.isEmpty()) {
	        this.version = Long.toString(System.currentTimeMillis()%100000);
	    }
	}
}
