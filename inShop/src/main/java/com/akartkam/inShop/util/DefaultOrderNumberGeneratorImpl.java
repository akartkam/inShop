package com.akartkam.inShop.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

public class DefaultOrderNumberGeneratorImpl implements OrderNumberGenerator {
	private static final Log LOG = LogFactory.getLog(DefaultOrderNumberGeneratorImpl.class);
	protected String prefix; 

	public DefaultOrderNumberGeneratorImpl() {
		
	}
	
	public DefaultOrderNumberGeneratorImpl (String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;

	}
	
	@Override
	public String generateOrderNumber(SessionImplementor session) {
		Connection connection = session.connection();
        try {
            PreparedStatement ps = connection
                    .prepareStatement("SELECT nextval ('order_number_generator') as nextval");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long n = rs.getLong("nextval");
                String on = prefix + String.format("%d", n);
                LOG.debug("Generated Order Number: " + on);
                return on;
            }
        } catch (SQLException e) {
            LOG.error(e);
            throw new HibernateException(
                    "Unable to generate default order number - " + e.getMessage());
        }
        return null;
	}

}
