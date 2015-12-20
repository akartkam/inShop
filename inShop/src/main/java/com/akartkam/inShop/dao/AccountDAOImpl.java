package com.akartkam.inShop.dao;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.akartkam.inShop.domain.Account;
import com.akartkam.inShop.service.product.ProductServiceImpl;

@Repository
public class AccountDAOImpl extends AbstractGenericDAO<Account> implements AccountDAO {

	private static final Log LOG = LogFactory.getLog(AccountDAOImpl.class);

	@Autowired 
	private JdbcTemplate jdbcTemplate;
	
	
	private static final String UPDATE_PASSWORD_SQL =
			"update account set password = ? where username = ?";
	
	@Override
	public void create(Account account, String password) {
		LOG.debug("Creating Account");
		create(account);
		currentSession().flush();
		updatePassword(account, password);
	}
	
	

	@Override
	public Account findByUsername(String username) {
		Query q = currentSession().getNamedQuery("findAccountByUsername");
		q.setParameter("username", username);
		return (Account) q.uniqueResult();
	}
	
	@Override
	public void updatePassword(Account account, String password) {
		LOG.debug("Update Account");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		jdbcTemplate.update(UPDATE_PASSWORD_SQL, hashedPassword, account.getUsername());		
	}

}
