import React from 'react';
import Header from '../component/share/Header';
import Footer from '../component/share/Footer';
import PostDetail from '../component/post-detail/PostDetail';
import LastedBlog from '../component/post-detail/LastedBlog';
import ExploreVN from '../component/share/ExploreVN';
import '../style/style.scss'

export default function PostDetailPage() {
    return (
        <>

            <div className="wrapper__post-detail-page container" id="post__dettail">
                <Header />
                <div className="row row__post-detail-page">
                    <div className="col-8 col__main">
                        <PostDetail />
                    </div>
                    <div className="col-4 col__last-blog">
                        <LastedBlog />
                    </div>
                </div>
                <div className="row row__explore">
                    <div className="col-12 col__explore">
                        <ExploreVN />
                    </div>
                </div>
                
            </div>
            <Footer />
        </>
    )
}