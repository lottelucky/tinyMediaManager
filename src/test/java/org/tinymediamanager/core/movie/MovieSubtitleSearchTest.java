package org.tinymediamanager.core.movie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tinymediamanager.core.MediaFileType;
import org.tinymediamanager.core.TmmModuleManager;
import org.tinymediamanager.core.entities.MediaFile;
import org.tinymediamanager.core.movie.entities.Movie;
import org.tinymediamanager.core.tvshow.TvShowModuleManager;
import org.tinymediamanager.scraper.MediaScraper;
import org.tinymediamanager.scraper.MediaSearchResult;
import org.tinymediamanager.scraper.ScraperType;
import org.tinymediamanager.scraper.SubtitleSearchOptions;
import org.tinymediamanager.scraper.mediaprovider.IMediaSubtitleProvider;

public class MovieSubtitleSearchTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TmmModuleManager.getInstance().startUp();
    MovieModuleManager.getInstance().startUp();
    TvShowModuleManager.getInstance().startUp();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    TvShowModuleManager.getInstance().shutDown();
    MovieModuleManager.getInstance().shutDown();
    TmmModuleManager.getInstance().shutDown();
  }

  @Test
  public void testSubtitleSearch() {
    // OpenSubtitles.org
    try {
      MediaScraper scraper = MediaScraper.getMediaScraperById("opensubtitles", ScraperType.SUBTITLE);
      assertThat(scraper).isNotNull();

      for (Movie movie : MovieList.getInstance().getMovies()) {
        for (MediaFile mediaFile : movie.getMediaFiles(MediaFileType.VIDEO)) {
          SubtitleSearchOptions options = new SubtitleSearchOptions(mediaFile.getFile());
          List<MediaSearchResult> results = ((IMediaSubtitleProvider) scraper.getMediaProvider()).search(options);
          if (!results.isEmpty()) {
            System.out.println("Subtitle for hash found: " + results.get(0).getUrl());
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }
}
