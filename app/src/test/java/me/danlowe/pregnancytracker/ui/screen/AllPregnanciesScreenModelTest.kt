package me.danlowe.pregnancytracker.ui.screen

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.danlowe.pregnancytracker.DbPregnancy
import me.danlowe.pregnancytracker.models.UiPregnancy
import me.danlowe.pregnancytracker.ui.screen.allpregnancies.RealAllPregnanciesScreenModel
import me.danlowe.testutils.testutils.TestDatabase
import me.danlowe.testutils.testutils.TestDateFormatter
import me.danlowe.testutils.testutils.TestDispatchers
import me.danlowe.utils.date.AppTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AllPregnanciesScreenModelTest {
  private lateinit var ut: RealAllPregnanciesScreenModel

  private val pregnancyQueries = TestDatabase().memoryPregnancyTracker.pregnancyQueries
  private val dispatchers = TestDispatchers(UnconfinedTestDispatcher())
  private val appDateFormatter = TestDateFormatter()
  private val appTime = object : AppTime {
    override fun currentUtcTimeAsString(): String = TEST_TIME
  }

  @Before
  fun setUp() {
    ut = RealAllPregnanciesScreenModel(pregnancyQueries, dispatchers, appDateFormatter, appTime)
  }

  @Test
  fun `addPregnancy should call insert on pregnancyQueries`() = runTest(dispatchers.testDispatcher) {
    val date = 123L
    val expected = listOf(
      DbPregnancy(1, "test", date.toString(), 1, TEST_TIME),
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

  companion object {
    private const val TEST_TIME = "2016-01-01T00:00:00"
  }
}
