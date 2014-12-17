/**
 * The model classes in this package were inspired by the v1.discovery file in this package (under resources).
 * Normally, you can use the bin/endpoints.sh script from the Google AppEngine SDK to generate
 * these model classes for you, but I couldn't get it to work so I just made the model classes
 * myself.
 *
 * If somebody wants to try it, just run (from the AppEngine SDK):
 *
 * bin/endpoints.sh gen-client-lib -l java -o /tmp/foo -bs maven ~/github.com/spotify/helios/helios-services/src/main/resources/com/spotify/helios/master/fleet/v1.discovery
 *
 * The discovery file itself comes from: https://github.com/coreos/fleet/blob/master/schema/v1.json
 */
package com.spotify.helios.master.fleet;