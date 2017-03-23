package com.bestroboticsteam.warehouseinterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import lejos.geom.Point;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.LineMap;

/***
 * Visualise an IGridMap on top of a LineMap.
 * 
 * @author Nick Hawes
 *
 */
public class GridMapVisualisation extends MapVisualisationComponent {
	private static final long serialVersionUID = 1L;

	protected IGridMap m_gridMap;

	public GridMapVisualisation(IGridMap _gridMap, LineMap _lineMap) {
		this(_gridMap, _lineMap, 100f);
	}

	public GridMapVisualisation(IGridMap _gridMap, LineMap _lineMap, float _scaleFactor) {
		super(_lineMap, _scaleFactor);
		m_gridMap = _gridMap;
	}

	private void connectToNeighbour(Graphics2D _g2, int _x, int _y, int _dx, int _dy) {

		if (m_gridMap.isValidTransition(_x, _y, _x + _dx, _y + _dy)) {
			Point p1 = m_gridMap.getCoordinatesOfGridPosition(_x, _y);
			Point p2 = m_gridMap.getCoordinatesOfGridPosition(_x + _dx, _y + _dy);
			renderLine(p1, p2, _g2);
		}

	}

	@Override
	protected void renderMap(Graphics2D _g2) {
		// render lines first
		super.renderMap(_g2);

		_g2.setStroke(new BasicStroke(1));
		_g2.setPaint(Color.BLUE);

		// add grid
		for (int x = 0; x < m_gridMap.getXSize(); x++) {
			for (int y = 0; y < m_gridMap.getYSize(); y++) {
				if (!m_gridMap.isObstructed(x, y)) {
					Point gridPoint = m_gridMap.getCoordinatesOfGridPosition(x, y);
					renderPoint(gridPoint, _g2, 0.02);
				}
			}
		}

		// and visualise valid connections
		for (int x = 0; x < m_gridMap.getXSize(); x++) {
			for (int y = 0; y < m_gridMap.getYSize(); y++) {

				if (m_gridMap.isValidGridPosition(x, y)) {
					connectToNeighbour(_g2, x, y, 1, 0);
					connectToNeighbour(_g2, x, y, 0, 1);
					connectToNeighbour(_g2, x, y, -1, 0);
					connectToNeighbour(_g2, x, y, 0, -1);
				}
			}
		}

	}

}
