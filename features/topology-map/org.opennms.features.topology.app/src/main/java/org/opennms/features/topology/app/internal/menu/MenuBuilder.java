/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.features.topology.app.internal.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.api.topo.VertexRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.vaadin.ui.MenuBar;


public class MenuBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(MenuBuilder.class);

	private static final String TOP_LEVEL_ADDITIONS = "Additions";

	private interface ItemAddBehaviour {

		MenuBar.MenuItem addItem(String caption);
	}

	protected List<MenuItem> m_menuBar = Lists.newArrayList();
	private List<String> m_menuOrder = new ArrayList<>();
	private Map<String, List<String>> m_submenuOrderMap = new HashMap<>();

	public void addMenuItem(MenuItem menuItem, String... parentPath) {
		if (parentPath == null || parentPath.length == 0) {
			m_menuBar.add(menuItem); // root element
		} else {
			// find menu item if exists
			MenuItem parent = findMenuItem(null, Arrays.asList(parentPath));
			if (parent == null) { // there is no parent for the current path, we create the path
				parent = addItems(parentPath);
			}
			parent.addChildren(menuItem);
		}
	}

	private MenuItem addItems(String... parentPath) {
		DefaultMenuItem newParent = new DefaultMenuItem(parentPath[0]);
		m_menuBar.add(newParent);
		for (String eachPath : Arrays.asList(parentPath).subList(1, parentPath.length)) {
			DefaultMenuItem childMenuItem = new DefaultMenuItem(eachPath);
			newParent.addChildren(childMenuItem);
			newParent = childMenuItem;
		}
		return newParent;
	}

	private MenuItem findMenuItem(MenuItem parent, List<String> parentPath) {
		if (parentPath.isEmpty()) {
			return parent;
		}
		List<MenuItem> items = getItemsToCheck(parent);
		if (items.isEmpty()) {
			return parent;
		}
		for (MenuItem eachItem : items) {
			if (eachItem.getLabel().equals(parentPath.get(0))) {
				return findMenuItem(eachItem, parentPath.subList(1, parentPath.size()));
			}
		}
		return null;
	}

	private List<MenuItem> getItemsToCheck(MenuItem parent) {
		if (parent == null) {
			return new ArrayList<>(m_menuBar);
		}
		return parent.getChildren();
	}


	public void setTopLevelMenuOrder(List<String> menuOrder) {
	    m_menuOrder = menuOrder;
	}

	public void setSubMenuGroupOrder(Map<String, List<String>> submenOrderMap) {
	    m_submenuOrderMap = submenOrderMap;
	}

	protected void determineAndApplyOrder() {
		for (MenuItem eachMenuItem : m_menuBar) {
			int order = determineOrderOfMenuEntry(eachMenuItem.getLabel(), m_menuOrder);
			eachMenuItem.setOrder(order);
			applyOrderToChildren(eachMenuItem);
		}
	}

	protected void applyOrderToChildren(final MenuItem parent) {
		final List<String> submenuOrder = m_submenuOrderMap.get(parent.getLabel()) != null ? m_submenuOrderMap.get(parent.getLabel()) :  m_submenuOrderMap.containsKey("default") ? m_submenuOrderMap.get("default") : new ArrayList<>();
		for (MenuItem eachChild : parent.getChildren()) {
			String groupLabel = getGroupForLabel(eachChild.getLabel(), submenuOrder);
			int order = determineOrderOfMenuEntry(groupLabel, submenuOrder);
			eachChild.setOrder(order);
		}
	}

	private int determineOrderOfMenuEntry(String label, List<String> menus) {
		if(menus.contains(label)) {
			return menus.indexOf(label);
		} else {
			if(menus.contains(TOP_LEVEL_ADDITIONS.toLowerCase())) {
				return menus.indexOf(TOP_LEVEL_ADDITIONS.toLowerCase());
			} else {
				return menus.size();
			}
		}
	}

	protected static String getGroupForLabel(String label, List<String> submenuOrder) {
		String group;
		String[] groupParams = label.split("\\?");

		for(String param : groupParams) {
			if(param.contains("group")) {
				String[] keyValue = param.split("=");
				group = keyValue[1];
				if (submenuOrder.contains(group)) {
					return group;
				}
				if (submenuOrder.contains(group.toLowerCase())) {
					return group.toLowerCase();
				}
			}
		}
		return null;
	}

	protected static String removeLabelProperties(String commandKey) {
		if(commandKey.contains("?")) {
			return commandKey.substring(0, commandKey.indexOf('?'));
		} else {
			return commandKey;
		}
	}

	public MenuBar build(MenuManager menuManager, List<VertexRef> targets, OperationContext operationContext) {
		determineAndApplyOrder();

		// Build root menu
		MenuBar menuBar = new MenuBar();
		List<MenuItem> rootItems = new ArrayList<>(m_menuBar);
		Collections.sort(rootItems);
		for (MenuItem eachRootElement : rootItems) {
			MenuBar.MenuItem menuItem = addItem(menuManager, (label) -> menuBar.addItem(label, null), eachRootElement, targets, operationContext);
			if (menuItem != null) {
				// Now add children
				List<MenuItem> childItems = new ArrayList<>(eachRootElement.getChildren());
				Collections.sort(childItems);
				final List<String> submenuOrder = m_submenuOrderMap.get(eachRootElement.getLabel()) != null ? m_submenuOrderMap.get(eachRootElement.getLabel()) : m_submenuOrderMap.containsKey("default") ? m_submenuOrderMap.get("default") : new ArrayList<>();
				String prevGroup = null;
				MenuBar.MenuItem prevMenuItem = null;
				for (MenuItem eachChild : childItems) {
					// add Separators between groups if the previous group changed and we added an element
					// (otherwise we may end up having multiple separators)
					String currentGroup = getGroupForLabel(eachChild.getLabel(), submenuOrder);
					if (prevGroup != null && prevMenuItem != null && !prevGroup.equals(currentGroup)) {
						menuItem.addSeparator();
					}
					prevGroup = currentGroup;
					prevMenuItem = addItem(menuManager, (label) -> menuItem.addItem(label, null), eachChild, targets, operationContext);
					// TODO add children of children (currently not supported)
				}
			}
		}
		return menuBar;
	}

	private static MenuBar.MenuItem addItem(MenuManager menuManager, ItemAddBehaviour behaviour, MenuItem eachChildElement, List<VertexRef> targets, OperationContext operationContext) {
		boolean visibility = eachChildElement.isVisible(targets, operationContext);
		if (visibility) { // only add item if it is actually visible
			final String caption = removeLabelProperties(eachChildElement.getLabel());
			final MenuBar.MenuItem childMenuItem = behaviour.addItem(caption);
			final boolean enabled = eachChildElement.isEnabled(targets, operationContext);
			final boolean checkable = eachChildElement.isCheckable();
			childMenuItem.setEnabled(enabled);
			childMenuItem.setCheckable(checkable);
			if (checkable) {
				boolean checked = eachChildElement.isChecked(targets, operationContext);
				childMenuItem.setChecked(checked);
			}

			// Add click behaviour if leaf element
			if (!eachChildElement.getChildren().isEmpty() && eachChildElement.getCommand() != null) {
				LOG.warn("The MenuItem {} is not a leaf but defines a command. The command is ignored.", caption);
			} else {
				if (eachChildElement.getCommand() != null) {
					childMenuItem.setCommand((MenuBar.Command) selectedItem -> {
						eachChildElement.getCommand().execute(targets, operationContext);
						menuManager.updateCommandListeners();
					});
				}
			}
			return childMenuItem;
		}
		return null;
	}
}
