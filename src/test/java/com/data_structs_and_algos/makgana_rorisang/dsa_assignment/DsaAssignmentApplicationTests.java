package com.data_structs_and_algos.makgana_rorisang.dsa_assignment;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb.Sorter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** The "test" profile disables UiLauncher, so no window opens. */
@SpringBootTest
@ActiveProfiles("test")
class DsaAssignmentApplicationTests {

	@Autowired
	private List<Sorter> sorters;

	@Test
	void contextLoads() {
	}

	@Test
	void allSortersAreRegistered() {
		assertEquals(3, sorters.size());
	}

}
