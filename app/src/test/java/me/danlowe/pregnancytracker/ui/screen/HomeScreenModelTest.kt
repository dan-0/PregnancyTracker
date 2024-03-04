package me.danlowe.pregnancytracker.ui.screen

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.danlowe.pregnancytracker.DbPregnancy
import me.danlowe.pregnancytracker.models.UiPregnancy
import me.danlowe.pregnancytracker.testutils.TestDatabase
import me.danlowe.pregnancytracker.testutils.TestDateFormatter
import me.danlowe.pregnancytracker.testutils.TestDispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenModelTest {
  private lateinit var ut: HomeScreenModel

  private val pregnancyQueries = TestDatabase().memoryPregnancyTracker.pregnancyQueries
  private val dispatchers = TestDispatchers(UnconfinedTestDispatcher())
  private val appDateFormatter = TestDateFormatter()

  @Before
  fun setUp() {
    ut = HomeScreenModel(pregnancyQueries, dispatchers, appDateFormatter)
  }

  @Test
  fun `addPregnancy should call insert on pregnancyQueries`() = runTest(dispatchers.testDispatcher) {
    val date = 123L
    val expected = listOf(
      DbPregnancy(1, "test", date.toString()),
    )
    ut.addPregnancy("test", date)

    val queries = pregnancyQueries.selectAll().executeAsList()

    assertEquals(expected, queries)
  }

  @Test
  fun `deletePregnancy should call delete on pregnancyQueries`() = runTest(
    dispatchers.testDispatcher,
  ) {
    val date = 123L
    ut.addPregnancy("test", date)
    val expected = emptyList<DbPregnancy>()
    ut.deletePregnancy(1)

    val queries = pregnancyQueries.selectAll().executeAsList()

    assertEquals(expected, queries)
  }

  @Test
  fun `items should return a list of UiPregnancy`() = runTest(dispatchers.testDispatcher) {
    val date = 123L
    ut.addPregnancy("test", date)
    val expected = listOf(
      UiPregnancy(1, date.toString(), "test"),
    )

    val items = ut.items.first()

    assertEquals(expected, items)
  }
}
