function addPin(map) {
	map.clearOverlays();
	map.addOverlay(new GMarker(map.getCenter()));
}